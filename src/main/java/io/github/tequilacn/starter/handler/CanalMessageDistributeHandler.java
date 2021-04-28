package io.github.tequilacn.starter.handler;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.google.protobuf.InvalidProtocolBufferException;
import io.github.tequilacn.starter.annotation.CanalEventListener;
import io.github.tequilacn.starter.listener.ICanalEventListener;
import io.github.tequilacn.starter.util.ApplicationContextUtil;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author Nacht
 * Created on 2021/4/12
 */
@Component
public class CanalMessageDistributeHandler {

    /**
     * 容器中的监听器list
     */
    private final List<ICanalEventListener> interfaceListeners = new ArrayList<>();

    public CanalMessageDistributeHandler(){
    }

    /**
     * 初始化所有监听器
     */
    @PostConstruct
    public void initListeners(){
        List<ICanalEventListener> canalEventListeners = ApplicationContextUtil.getBeansOfType(ICanalEventListener.class);
        /*过滤掉未被CanalEventListener注解修饰的类实例*/
        Predicate<ICanalEventListener> annotatedFilter = bean -> bean.getClass().isAnnotationPresent(CanalEventListener.class);
        interfaceListeners.addAll(canalEventListeners.stream().filter(annotatedFilter).collect(Collectors.toList()));
    }


    /**
     * 分发消息中的事件
     * @param message 消息
     * @param destination 消息来源的canal destination
     */
    @SneakyThrows
    public void distribute(Message message, String destination) {
        message.getEntries().forEach(entry -> {
            /*如果不是行数据变更的binlog, 不做处理*/
            if(!CanalEntry.EntryType.ROWDATA.equals(entry.getEntryType())){
                return;
            }
            CanalEntry.RowChange rowChange = null;
            try {
                rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
            }
            final CanalEntry.EventType eventType = Objects.requireNonNull(rowChange).getEventType();
            /*查询或者ddl语句不做处理*/
            if (eventType.equals(CanalEntry.EventType.QUERY) || rowChange.getIsDdl()) {
                return;
            }
            final String schema = entry.getHeader().getSchemaName();
            final String tableName = entry.getHeader().getTableName();
            /*对每一行的rowData进行分发*/
            rowChange.getRowDatasList().forEach(rowData -> {
                List<CanalEntry.Column> beforeColumns = rowData.getBeforeColumnsList();
                List<CanalEntry.Column> afterColumns = rowData.getAfterColumnsList();
                interfaceListeners.stream()
                        .filter(interfaceListener -> this.isListenerSourceMatch(interfaceListener, destination, schema, tableName))
                        .forEach(interfaceListener -> interfaceListener.onEvent(eventType, beforeColumns, afterColumns));
            });
        });
    }

    /**
     * 判断消息的destination, schema, tableName是否和canal监听器一致的过滤条件(消息和监听器在注解中配置的条件一致才会被分发到对应的监听器中)
     * @param listener 监听器实例
     * @param destination 消息来源的canal destination
     * @param schema 消息来源的schema
     * @param tableName 消息来源的tableName
     * @return buildListenerSourceIsMatch
     */
    private boolean isListenerSourceMatch(ICanalEventListener listener, String destination, String schema, String tableName){
        CanalEventListener annotation = listener.getClass().getAnnotation(CanalEventListener.class);
        boolean destinationMatch = Arrays.stream(annotation.destinations()).anyMatch(StringUtils :: isEmpty) || Arrays.asList(annotation.destinations()).contains(destination);
        boolean schemaMatch = Arrays.stream(annotation.schemas()).anyMatch(StringUtils :: isEmpty) || Arrays.asList(annotation.schemas()).contains(schema);
        boolean tableNameMatch = Arrays.stream(annotation.tableNames()).anyMatch(StringUtils :: isEmpty) || Arrays.asList(annotation.tableNames()).contains(tableName);
        return destinationMatch && schemaMatch && tableNameMatch;
    }

}
