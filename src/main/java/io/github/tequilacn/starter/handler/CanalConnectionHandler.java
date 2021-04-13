package io.github.tequilacn.starter.handler;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import io.github.tequilacn.starter.config.CanalConnectorProperties;
import io.github.tequilacn.starter.handler.executor.CanalMessageExecutor;
import lombok.extern.slf4j.Slf4j;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * 管理所有的canal实例连接和初始化
 * @author Nacht
 * Created on 2021/4/11
 */
@Slf4j
public class CanalConnectionHandler {

    /**
     * 消息分发处理器
     */
    private CanalMessageDistributeHandler canalMessageDistributeHandler;

    /**
     * canal实例连接集合
     */
    private final List<CanalConnector> canalConnectors;

    /**
     * 执行监听任务的线程池
     */
    private final ThreadPoolExecutor executor;

    /**
     * canal连接配置
     */
    private final CanalConnectorProperties canalConnectorProperties;

    public CanalConnectionHandler(CanalConnectorProperties canalConnectorProperties, CanalMessageDistributeHandler messageDistributeHandler) {
        this.canalConnectors = new ArrayList<>();
        this.canalConnectorProperties = canalConnectorProperties;
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        this.executor = new ThreadPoolExecutor(10, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<>(), threadFactory);
        this.canalMessageDistributeHandler = messageDistributeHandler;
    }

    /**
     * 初始化连接, 并为每个连接开启线程监听
     */
    public void init() {
        this.initCanalConnectors();
    }

    /**
     * 初始化配置中的所有canal连接, 为其分配监听线程
     */
    private void initCanalConnectors() {
        canalConnectorProperties.getInstances()
                .entrySet()
                .forEach(entry -> {
                    CanalConnector canalConnector = initCanalConnector(entry);
                    canalConnectors.add(canalConnector);
                    executor.execute(new CanalMessageExecutor(canalConnector, entry.getValue(), "CanalMessageExecutor-" + entry.getKey(), canalMessageDistributeHandler));
                });
    }

    /**
     * 关闭所有connector
     */
    public void close(){
        canalConnectors.forEach(CanalConnector::disconnect);
    }

    /**
     * 初始化单个canal instance的连接
     * @param configEntry key->instance destination value-> instance properties
     * @return
     */
    private CanalConnector initCanalConnector(Map.Entry<String, CanalConnectorProperties.InstanceProperties> configEntry){
        String destination = configEntry.getKey();
        CanalConnectorProperties.InstanceProperties instanceProperties = configEntry.getValue();
        log.info("初始化[destination->{}] Canal连接 ===============> BEGIN", destination);
        InetSocketAddress inetSocketAddress = new InetSocketAddress(instanceProperties.getServer(), instanceProperties.getPort());
        CanalConnector connector = CanalConnectors.newSingleConnector(inetSocketAddress, destination, instanceProperties.getUserName(), instanceProperties.getPassword());
        connector.connect();
        connector.subscribe(instanceProperties.getFilter());
        connector.rollback();
        log.info("初始化[destination->{}] Canal连接 ===============> FINISHED", destination);
        return connector;
    }

}
