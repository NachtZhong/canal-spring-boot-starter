package com.nacht.starter.config;

import com.alibaba.otter.canal.client.CanalConnector;
import com.alibaba.otter.canal.client.CanalConnectors;
import com.nacht.starter.handler.CanalConnectionHandler;
import com.nacht.starter.util.ApplicationContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;


/**
 * @author Nacht
 * Created on 2021/4/10
 */
@Configuration
@ConditionalOnClass(CanalConnectorProperties.class)
@EnableConfigurationProperties(CanalConnectorProperties.class)
@Slf4j
public class CanalConnectorAutoConfigure {

    /**
     * 注入配置
     */
    @Autowired
    private CanalConnectorProperties canalConnectorProperties;


    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ApplicationContextUtil applicationContextUtil(){
        return new ApplicationContextUtil();
    }

    @Bean
    public CanalConnectionHandler canalConnectionHandler(){
        CanalConnectionHandler canalConnectionHandler = new CanalConnectionHandler(this.canalConnectorProperties);
        canalConnectionHandler.init();
        return canalConnectionHandler;
    }


}
