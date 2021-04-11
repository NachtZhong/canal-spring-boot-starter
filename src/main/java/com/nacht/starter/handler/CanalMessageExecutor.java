package com.nacht.starter.handler;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.Message;
import com.nacht.starter.config.CanalConnectorProperties;

/**
 * canal实例监听线程
 * @author Nacht
 * Created on 2021/4/12
 */
public class CanalMessageExecutor implements Runnable{

    /**
     * 当前执行器对应的instance配置
     */
    private final CanalConnectorProperties.InstanceProperties instanceProperties;

    /**
     * 当前执行器对应的canal连接
     */
    private final CanalConnector canalConnector;

    /**
     * 线程名称
     */
    private String threadName;

    /**
     * canal实例监听线程构造器
     * @param canalConnector canal connector连接实例
     * @param instanceProperties canal 实例配置
     * @param threadName 线程名称
     */
    public CanalMessageExecutor(CanalConnector canalConnector, CanalConnectorProperties.InstanceProperties instanceProperties, String threadName){
        this.canalConnector = canalConnector;
        this.instanceProperties = instanceProperties;
        this.threadName = threadName;
    }

    /**
     * 启动监听, 通过消息分发器进行分发
     */
    @Override
    public void run() {
        Thread.currentThread().setName(threadName);
        final int batchSize = instanceProperties.getBatchSize();
        final long sleepAfterFailAcquire = instanceProperties.getSleepAfterFailAcquire();
        while(!Thread.currentThread().isInterrupted()){
            Message message = canalConnector.getWithoutAck(batchSize);

        }
    }
}
