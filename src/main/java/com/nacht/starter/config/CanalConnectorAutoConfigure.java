package com.nacht.starter.config;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.InetSocketAddress;

/**
 * @author Nacht
 * Created on 2021/4/10
 */
@Configuration
@ConditionalOnClass(CanalConnectorProperties.class)
@EnableConfigurationProperties(CanalConnectorProperties.class)
@Slf4j
public class CanalConnectorAutoConfigure {

    private final CanalConnectorProperties canalConnectorProperties;

    @Autowired
    public CanalConnectorAutoConfigure(CanalConnectorProperties properties){
        this.canalConnectorProperties = properties;
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "canal", value = "enabled", havingValue = "true")
    public CanalConnector canalConnector(){
        log.info("初始化Canal连接");
        CanalConnector connector = CanalConnectors.newSingleConnector(new InetSocketAddress(canalConnectorProperties.getServer(), canalConnectorProperties.getPort()),
                canalConnectorProperties.getDestination(), "", "");
        connector.connect();
        connector.subscribe();
        return connector;
    }

}
