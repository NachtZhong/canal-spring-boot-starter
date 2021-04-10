package com.nacht.starter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author Nacht
 * Created on 2021/4/10
 */
@ConfigurationProperties("canal")
@Data
public class CanalConnectorProperties {
    /**
     * canal 服务端地址
     */
    private String server;
    /**
     * canal 服务端端口
     */
    private int port;
    /**
     * canal 实例
     */
    private String destination;

}
