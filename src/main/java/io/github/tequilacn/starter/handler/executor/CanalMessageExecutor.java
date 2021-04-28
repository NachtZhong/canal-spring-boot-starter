package io.github.tequilacn.starter.handler.executor;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.protocol.Message;
import io.github.tequilacn.starter.config.CanalConnectorProperties;
import io.github.tequilacn.starter.handler.CanalMessageDistributeHandler;
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
     * 该connector对应的instance destination
     */
    private final String destination;

    /**
     * canal实例监听线程构造器
     * @param canalConnector canal connector连接实例
     * @param destination 该connector对应的instance destination
     * @param instanceProperties canal 实例配置
     * @param threadName 线程名称
     * @param messageDistributeHandler 消息分发处理器
     */
    public CanalMessageExecutor(CanalConnector canalConnector,
                                String destination,
                                CanalConnectorProperties.InstanceProperties instanceProperties,
                                String threadName,
                                CanalMessageDistributeHandler messageDistributeHandler){
        this.canalConnector = canalConnector;
        this.destination = destination;
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
            try {
                log.info("{}: 开始处理{}条数据", threadName, size);
                messageDistributeHandler.distribute(message, destination);
                canalConnector.ack(batchId);
                log.info("{}: 完成处理{}条数据, 向server端发送ack确认处理完成", threadName, size);
            }catch (Exception e){
                log.error(threadName + ": batchId[" + batchId + "], 处理过程中发生了一个异常:", e);
                canalConnector.rollback(batchId);
            }
        }
    }
}
