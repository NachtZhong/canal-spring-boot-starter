package io.github.tequilacn.starter.listener;

import com.alibaba.otter.canal.protocol.CanalEntry;

import java.util.List;

/**
 * canal dml事件处理器接口(原始column模式)
 * @author Nacht
 * Created on 2021/4/12
 */
public interface CanalDmlRawEventListener extends ICanalEventListener {

    /**
     * 根据类型分发dml事件
     */
    @Override
    default void onEvent(CanalEntry.EventType eventType, List<CanalEntry.Column> beforeColumns, List<CanalEntry.Column> afterColumns) {
        switch (eventType){
            case INSERT:
                this.onInsert(afterColumns);
                break;
            case UPDATE:
                this.onUpdate(beforeColumns, afterColumns);
                break;
            case DELETE:
                this.onDelete(beforeColumns);
                break;
            default:
        }
    }

    /**
     * 处理insert事件
     * @param afterColumns 新增数据列
     */
    void onInsert(List<CanalEntry.Column> afterColumns);

    /**
     * 处理update事件
     */
    void onUpdate(List<CanalEntry.Column> beforeColumns, List<CanalEntry.Column> afterColumns);

    /**
     * 处理delete事件
     */
    void onDelete(List<CanalEntry.Column> beforeColumns);


}
