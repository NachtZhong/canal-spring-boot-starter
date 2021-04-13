package io.github.tequilacn.starter.listener;

import com.alibaba.otter.canal.protocol.CanalEntry;

import java.util.List;

/**
 * @author Nacht
 * Created on 2021/4/12
 */
public interface CanalEventListener {

    /**
     * canal事件处理器
     * @param eventType
     * @param beforeColumns
     * @param afterColumns
     */
    void onEvent(CanalEntry.EventType eventType, List<CanalEntry.Column> beforeColumns, List<CanalEntry.Column> afterColumns);

}
