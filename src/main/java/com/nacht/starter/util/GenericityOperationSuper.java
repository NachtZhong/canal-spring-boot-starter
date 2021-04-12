package com.nacht.starter.util;

import lombok.Data;

import java.lang.reflect.ParameterizedType;

@Data
public class GenericityOperationSuper<T> {

    /**
     * 泛型的类型
     */
    private Class<T> entityClass;

    private int index = 0;

    public GenericityOperationSuper() {
        BaseHibernateEntityDao();
    }

    @SuppressWarnings("unchecked")
    public void BaseHibernateEntityDao() {
        entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[index];
    }


}