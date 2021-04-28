package io.github.tequilacn.starter.util;

import com.alibaba.otter.canal.protocol.CanalEntry;
import io.github.tequilacn.starter.annotation.TableField;
import io.github.tequilacn.starter.reflect.PrimitiveTypes;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 启停配置的所有canal实例
 * @author Nacht
 * Created on 2021/4/11
 */
@Slf4j
public class CanalUtil {

    /**
     * 将列转换为实体
     */
    @SneakyThrows
    public static <T> T transformColumnsToBean(List<CanalEntry.Column> columns, Class<T> clazz) {
        T result = clazz.newInstance();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            Optional<String> valueOptional = columns.stream()
                    .filter(column -> column.getName().equals(translateFieldNameToColumnName(field)))
                    .findFirst()
                    .map(CanalEntry.Column::getValue);
            valueOptional.ifPresent(value -> {
                try {
                    field.set(result, getRealTypeValue(field.getType(), value));
                } catch (IllegalAccessException e) {
                    log.error("class:" + clazz.getName() + ", field:" + field.getName() + "cast failed:", e);
                }
            });
        }
        return result;
    }

    /**
     * 根据成员变量类型获取真正类型对应的值
     * @param value 值的字符串值
     * @param <T> T
     * @return realValue
     */
    @SneakyThrows
    private static <T> T getRealTypeValue(Class<?> clazz, String value) {
        if(clazz.isPrimitive()){
            return (T) PrimitiveTypes.getWrapper(clazz).getDeclaredMethod("valueOf", String.class).invoke(null, value);
        }
        if(clazz == Date.class){
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return StringUtils.isEmpty(value) ? null : (T) Date.from(LocalDateTime.parse(value, dateTimeFormatter).atZone(ZoneId.systemDefault()).toInstant());
        }
        if(clazz == String.class){
            return (T) value;
        }
        return (T) clazz.getDeclaredMethod("valueOf", String.class).invoke(null, value);
    }

    /**
     * 将成员变量name根据注解翻译成对应的库表字段名, 如果没有TableField注解, 默认采用驼峰转换
     * @param field 成员变量
     * @return tableFieldName
     */
    private static String translateFieldNameToColumnName(Field field){
        return field.isAnnotationPresent(TableField.class) ? field.getAnnotation(TableField.class).value() : CamelCaseUtil.toLine(field.getName());
    }

}
