package com.nacht.starter.annotation;

import com.nacht.starter.config.CanalConnectorAutoConfigure;
import com.nacht.starter.config.CanalConnectorProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 启用canal
 * @author Nacht
 * Created on 2021/4/11
 */
@Retention(RetentionPolicy.RUNTIME)
@Import({CanalConnectorProperties.class, CanalConnectorAutoConfigure.class})
public @interface EnableCanal {
}
