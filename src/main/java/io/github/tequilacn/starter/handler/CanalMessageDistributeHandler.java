package io.github.tequilacn.starter.handler;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.alibaba.otter.canal.protocol.Message;
import com.google.protobuf.InvalidProtocolBufferException;
import io.github.tequilacn.starter.listener.CanalEventListener;
import io.github.tequilacn.starter.util.ApplicationContextUtil;
import lombok.SneakyThrows;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Nacht
 * Created on 2021/4/12
 */
public class CanalMessageDistributeHandler {

    private final List<CanalEventListener> interfaceListeners = new ArrayList<>();

    public CanalMessageDistributeHandler(){
    }

    /**
     * 初始化所有监听器
     */
    @PostConstruct
    public void initListeners(){
        List<CanalEventListener> canalEventListeners = ApplicationContextUtil.getBeansOfType(CanalEventListener.class);
        this.interfaceListeners.addAll(canalEventListeners);
    }

    /**
     * 分发消息中的事件
     * @param message
     */
    @SneakyThrows
    public void distribute(Message message) {
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
            final CanalEntry.EventType eventType = rowChange.getEventType();
            /*查询或者ddl语句不做处理*/
            if (eventType.equals(CanalEntry.EventType.QUERY) || rowChange.getIsDdl()) {
                return;
            }
            /*对每一行的rowData进行分发*/
            rowChange.getRowDatasList().forEach(rowData -> {
                List<CanalEntry.Column> beforeColumns = rowData.getBeforeColumnsList();
                List<CanalEntry.Column> afterColumns = rowData.getAfterColumnsList();
                interfaceListeners.forEach(interfaceListener -> interfaceListener.onEvent(eventType, beforeColumns, afterColumns));
            });
        });
    }
}
