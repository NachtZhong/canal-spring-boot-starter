package io.github.tequilacn.starter.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于数据库表字段和实体类字段非一一对应的场景, 标记字段对应的库表字段
 * @author Nacht
 * Created on 2021/4/27
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TableField {
    /**
     * 对应数据库表的字段
     */
    String value();
}
