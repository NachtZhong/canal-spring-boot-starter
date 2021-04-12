package com.nacht.starter.config;

import com.nacht.starter.handler.CanalConnectionHandler;
import com.nacht.starter.handler.CanalMessageDistributeHandler;
import com.nacht.starter.util.ApplicationContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;


/**
 * @author Nacht
 * Created on 2021/4/10
 */
//@Configuration
@ConditionalOnClass(CanalConnectorProperties.class)
@Slf4j
public class CanalConnectorAutoConfigure {

    /**
     * 注入配置
     */
    @Autowired(required = false)
    private CanalConnectorProperties canalConnectorProperties;


    /**
     * spring上下文工具bean
     * @return
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ApplicationContextUtil applicationContextUtil(){
        return new ApplicationContextUtil();
    }

    /**
     * canal实例连接处理器bean
     * @return
     */
    @Bean
    public CanalConnectionHandler canalConnectionHandler(){
        CanalConnectionHandler canalConnectionHandler = new CanalConnectionHandler(this.canalConnectorProperties, canalMessageDistributeHandler());
        canalConnectionHandler.init();
        return canalConnectionHandler;
    }

    /**
     * canal消息分发处理器bean
     * @return
     */
    @Bean
    public CanalMessageDistributeHandler canalMessageDistributeHandler(){
        return new CanalMessageDistributeHandler();
    }
}
