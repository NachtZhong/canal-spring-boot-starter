package io.github.tequilacn.starter.listener;

import com.alibaba.otter.canal.protocol.CanalEntry;
import io.github.tequilacn.starter.util.CanalUtil;

import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * canal dml事件处理器接口(bean模式)
 * @author Nacht
 * Created on 2021/4/12
 */
public interface CanalDmlBeanEventListener<T> extends ICanalEventListener {


    /**
     * 根据类型分发dml事件
     * do not override it !!!
     */
    @Override
    default void onEvent(CanalEntry.EventType eventType, List<CanalEntry.Column> beforeColumns, List<CanalEntry.Column> afterColumns) {
        ParameterizedType parameterizedType = (ParameterizedType) this.getClass().getGenericInterfaces()[0];
        Class<T> clazz = (Class<T>) parameterizedType.getActualTypeArguments()[0];
        switch (eventType){
            case INSERT:
                this.onInsertBean(CanalUtil.transformColumnsToBean(afterColumns, clazz));
                break;
            case UPDATE:
                this.onUpdateBean(CanalUtil.transformColumnsToBean(beforeColumns, clazz), CanalUtil.transformColumnsToBean(afterColumns, clazz));
                break;
            case DELETE:
                this.onDeleteBean(CanalUtil.transformColumnsToBean(beforeColumns, clazz));
                break;
            default:
        }
    }

    /**
     * 处理新增实体
     * @param afterRow 新增后的行朱提
     */
    void onInsertBean(T afterRow);

    /**
     * 处理更新前后实体
     * @param beforeRow 更新前行实体
     * @param afterRow 更新后行实体
     */
    void onUpdateBean(T beforeRow, T afterRow);

    /**
     * 处理删除实体
     * @param beforeRow 删除前行实体
     */
    void onDeleteBean(T beforeRow);

}
