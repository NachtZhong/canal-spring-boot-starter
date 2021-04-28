package io.github.tequilacn.starter.listener;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.google.common.base.Preconditions;
import io.github.tequilacn.starter.annotation.CanalEventListener;

import java.util.List;

/**
 * @author Nacht
 * Created on 2021/4/12
 */
public interface ICanalEventListener {

    /**
     * canal事件处理器
     */
    void onEvent(CanalEntry.EventType eventType, List<CanalEntry.Column> beforeColumns, List<CanalEntry.Column> afterColumns);

}
