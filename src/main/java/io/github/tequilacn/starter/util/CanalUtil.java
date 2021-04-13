package io.github.tequilacn.starter.util;

import com.alibaba.otter.canal.protocol.CanalEntry;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 启停配置的所有canal实例
 * @author Nacht
 * Created on 2021/4/11
 */
public class CanalUtil {

    /**
     * 将列转换为实体
     * @return
     */
    @SneakyThrows
    public static <T> T transformColumnsToBean(List<CanalEntry.Column> columns, Class<T> clazz) {
        T result = clazz.newInstance();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            Object value = columns.stream()
                    .filter(column -> CamelCaseUtil.underscoreToCamelCase(column.getName()).equals(field.getName()))
                    .findFirst()
                    .map(CanalEntry.Column::getValue)
                    .orElse(null);
            field.set(result, value);
        }
        return result;
    }

}
