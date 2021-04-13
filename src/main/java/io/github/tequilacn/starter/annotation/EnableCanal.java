package io.github.tequilacn.starter.annotation;

import io.github.tequilacn.starter.config.CanalConnectorAutoConfigure;
import io.github.tequilacn.starter.config.CanalConnectorProperties;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启用canal
 * @author Nacht
 * Created on 2021/4/11
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({CanalConnectorProperties.class, CanalConnectorAutoConfigure.class})
public @interface EnableCanal {
}
