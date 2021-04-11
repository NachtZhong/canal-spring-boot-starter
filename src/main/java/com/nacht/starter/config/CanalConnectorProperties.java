package com.nacht.starter.config;

import lombok.Data;
import lombok.NonNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Nacht
 * Created on 2021/4/10
 */
@Data
@ConfigurationProperties("canal")
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CanalConnectorProperties {

    /**
     * instance配置map, key为destination, value为每个instance的配置值
     */
    @NonNull
    private Map<String, InstanceProperties> instances = new LinkedHashMap<>();

    @Data
    public static class InstanceProperties{

        /**
         * canal 服务端地址
         */
        private String server = "127.0.0.1";

        /**
         * canal 服务端端口
         */
        private int port = 11111;

        /**
         * 用户名
         */
        private String userName = "";

        /**
         * 密码
         */
        private String password = "";

        /**
         * 单批次处理数
         */
        private int batchSize = 100;

        /**
         * 没取到数据后的休眠时间(ms)
         */
        private long sleepAfterFailAcquire = 1000L;

        /**
         * 订阅表过滤
         */
        private String filter = "";
    }

}
