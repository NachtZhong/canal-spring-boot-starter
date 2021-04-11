//package com.nacht.starter.task;
//
//import com.alibaba.otter.canal.client.CanalConnector;
//import com.alibaba.otter.canal.protocol.CanalEntry;
//import com.alibaba.otter.canal.protocol.Message;
//import com.google.protobuf.InvalidProtocolBufferException;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
///**
// * @author Nacht
// * Created on 2021/4/10
// */
//@Component
//@EnableScheduling
//@Slf4j
//public class CanalMessageListenTask {
//
//    /**
//     * 缓存更新间隔
//     */
//    private static final long TASK_INTERVAL = 1000L;
//
//    @Autowired
//    CanalConnector canalConnector;
//
//    /**
//     * 更新缓存
//     */
//    @Scheduled(fixedRate = TASK_INTERVAL)
//     public void updateCache(){
//        try{
//            log.info("开始拉取canal信息");
//            Message message = canalConnector.getWithoutAck(1000);
//
//            long batchID = message.getId();
//
//            int size = message.getEntries().size();
//
//            if (batchID == -1 || size == 0) {
//                log.info("当前暂时没有数据");
//                return;
//            }
//            handleEntry(message.getEntries());
//            log.info("canal信息拉取完成");
//        }catch(Exception e){
//            log.error("更新*缓存时发生了一个异常, 异常详情:", e);
//        }
//     }
//
//    /**
//     * 处理canal拉取到的信息
//     * @param entries
//     */
//    private void handleEntry(List<CanalEntry.Entry> entries) {
//        for (CanalEntry.Entry entry : entries) {
//
//            // 第一步：拆解entry 实体
//            CanalEntry.Header header = entry.getHeader();
//            CanalEntry.EntryType entryType = entry.getEntryType();
//
//            // 第二步： 如果当前是RowData，那就是我需要的数据
//            if (entryType == CanalEntry.EntryType.ROWDATA) {
//
//                String tableName = header.getTableName();
//                String schemaName = header.getSchemaName();
//
//                CanalEntry.RowChange rowChange = null;
//
//                try {
//                    rowChange = CanalEntry.RowChange.parseFrom(entry.getStoreValue());
//                } catch (InvalidProtocolBufferException e) {
//                    e.printStackTrace();
//                }
//
//                CanalEntry.EventType eventType = rowChange.getEventType();
//
//                log.info(String.format("当前正在操作表 %s.%s， 执行操作= %s", schemaName, tableName, eventType));
//
//                // 如果是‘查询’ 或者 是 ‘DDL’ 操作，那么sql直接打出来
//                if (eventType == CanalEntry.EventType.QUERY || rowChange.getIsDdl()) {
//                    log.info("执行了查询语句：[{}]", rowChange.getSql());
//                    return;
//                }
//
//                // 第三步：追踪到 columns 级别
//                rowChange.getRowDatasList().forEach((rowData) -> {
//
//                    // 获取更新之前的column情况
//                    List<CanalEntry.Column> beforeColumns = rowData.getBeforeColumnsList();
//
//                    // 获取更新之后的 column 情况
//                    List<CanalEntry.Column> afterColumns = rowData.getAfterColumnsList();
//
//                    // 当前执行的是 删除操作
//                    if (eventType == CanalEntry.EventType.DELETE) {
//                        printColumn(beforeColumns);
//                    }
//
//                    // 当前执行的是 插入操作
//                    if (eventType == CanalEntry.EventType.INSERT) {
//                        printColumn(afterColumns);
//                    }
//
//                    // 当前执行的是 更新操作
//                    if (eventType == CanalEntry.EventType.UPDATE) {
//                        printColumn(beforeColumns);
//                        printColumn(afterColumns);
//                    }
//
//
//                });
//            }
//        }
//    }
//    /**
//     * 每个row上面的每一个column 的更改情况
//     * @param columns
//     */
//    public static void printColumn(List<CanalEntry.Column> columns) {
//
//        columns.forEach((column) -> {
//            String columnName = column.getName();
//            String columnValue = column.getValue();
//            String columnType = column.getMysqlType();
//            // 判断 该字段是否更新
//            boolean isUpdated = column.getUpdated();
//            log.info(String.format("数据列：columnName=%s, columnValue=%s, columnType=%s, isUpdated=%s", columnName, columnValue, columnType, isUpdated));
//        });
//    }
//}
