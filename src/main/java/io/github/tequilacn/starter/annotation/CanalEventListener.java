package io.github.tequilacn.starter.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记一个对象为canal的监听器
 * @author Nacht
 * Created on 2021/4/12
 */
@Component
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface CanalEventListener {

    /**
     * 监听器监听的实例, 值为空表示所有(使用Bean模式的监听器时要精确配置)
     * @return destination
     */
    String[] destinations() default "";

    /**
     * 监听器监听的数据库名, 值为空表示所有(使用Bean模式的监听器时要精确配置)
     * @return schemas
     */
    String[] schemas() default "";

    /**
     * 监听器监听的表名, 值为空表示所有(使用Bean模式的监听器时要精确配置)
     * @return tableNames
     */
    String[] tableNames() default "";
}
