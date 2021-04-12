package com.nacht.starter.handler.executor;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.Message;
import com.nacht.starter.config.CanalConnectorProperties;
import com.nacht.starter.handler.CanalMessageDistributeHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * canal实例监听线程
 * @author Nacht
 * Created on 2021/4/12
 */
@Slf4j
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
    private final String threadName;

    /**
     * 消息分发处理器
     */
    private final CanalMessageDistributeHandler messageDistributeHandler;

    /**
     * canal实例监听线程构造器
     * @param canalConnector canal connector连接实例
     * @param instanceProperties canal 实例配置
     * @param threadName 线程名称
     * @param messageDistributeHandler 消息分发处理器
     */
    public CanalMessageExecutor(CanalConnector canalConnector,
                                CanalConnectorProperties.InstanceProperties instanceProperties,
                                String threadName,
                                CanalMessageDistributeHandler messageDistributeHandler){
        this.canalConnector = canalConnector;
        this.instanceProperties = instanceProperties;
        this.threadName = threadName;
        this.messageDistributeHandler = messageDistributeHandler;
    }

    /**
     * 启动监听, 通过消息分发器进行分发
     */
    @Override
    @SneakyThrows
    public void run() {
        Thread.currentThread().setName(threadName);
        final int batchSize = instanceProperties.getBatchSize();
        final long sleepAfterFailAcquire = instanceProperties.getSleepAfterFailAcquire();
        while(!Thread.currentThread().isInterrupted()){
            Message message = canalConnector.getWithoutAck(batchSize);
            long batchId = message.getId();
            int size = message.getEntries().size();
            /*如果没有数据, 休眠一段时间后重试*/
            if (batchId == -1 || size == 0) {
                TimeUnit.MILLISECONDS.sleep(sleepAfterFailAcquire);
                continue;
            }
            messageDistributeHandler.distribute(message);
        }
    }
}
