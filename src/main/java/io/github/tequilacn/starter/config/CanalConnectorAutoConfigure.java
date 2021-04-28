package io.github.tequilacn.starter.config;

import io.github.tequilacn.starter.handler.CanalConnectionHandler;
import io.github.tequilacn.starter.handler.CanalMessageDistributeHandler;
import io.github.tequilacn.starter.util.ApplicationContextUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
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
     * spring上下文工具bean
     */
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public ApplicationContextUtil applicationContextUtil(){
        return new ApplicationContextUtil();
    }

    /**
     * canal消息分发处理器bean
     */
    @Bean
    public CanalMessageDistributeHandler canalMessageDistributeHandler(){
        return new CanalMessageDistributeHandler();
    }

    /**
     * canal实例连接处理器bean
     */
    @Bean
    public CanalConnectionHandler canalConnectionHandler(CanalConnectorProperties canalConnectorProperties, CanalMessageDistributeHandler canalMessageDistributeHandler){
        CanalConnectionHandler canalConnectionHandler = new CanalConnectionHandler(canalConnectorProperties, canalMessageDistributeHandler);
        canalConnectionHandler.init();
        return canalConnectionHandler;
    }


}
