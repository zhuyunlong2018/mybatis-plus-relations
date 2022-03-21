package com.zyl.mybatisplus.relations;

import com.zyl.mybatisplus.relations.func.IGetter;
import com.zyl.mybatisplus.relations.handler.*;
import com.zyl.mybatisplus.relations.utils.BeanUtils;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Relations {

    public static Map<String, RelationCache> relationMap = new HashMap<>();

    public static String cacheKey(Class<?> clazz, String field) {
        return clazz.getName() + "." + field;
    }

    /**
     * bindMany关联模型，使用lambda getter方法
     */
    public static <T> Handler<T> withMany(List<T> list, IGetter<T, ?> propertyGetter) {
        String property = BeanUtils.convertToFieldName(propertyGetter);
        return withMany(list, property);
    }

    /**
     * bindMany关联模型，使用属性字符串
     */
    public static <T> Handler<T> withMany(List<T> list, String propertyName) {
        ListBindManyHandler<T> tWithRelations = new ListBindManyHandler<>(list);
        return tWithRelations.bind(propertyName);
    }

    /**
     * bindMany关联模型
     */
    public static <T> Handler<T> withMany(List<T> list) {
        return new ListBindManyHandler<>(list);
    }

    // <========================================================

    /**
     * bindMany关联模型
     */
    public static <T> Handler<T> withMany(T entity, IGetter<T, ?> propertyGetter) {
        String property = BeanUtils.convertToFieldName(propertyGetter);
        return withMany(entity, property);
    }

    /**
     * bindMany关联模型
     */
    public static <T> Handler<T> withMany(T entity, String propertyName) {
        EntityBindManyHandler<T> tWithRelations = new EntityBindManyHandler<>(entity);
        return tWithRelations.bind(propertyName);
    }

    /**
     * bindMany关联模型
     */
    public static <T> Handler<T> withMany(T entity) {
        return new EntityBindManyHandler<>(entity);
    }

    // <========================================================

    /**
     * bindOne关联模型
     */
    public static <T> Handler<T> withOne(List<T> list, IGetter<T, ?> propertyGetter) {
        String property = BeanUtils.convertToFieldName(propertyGetter);
        return withOne(list, property);
    }

    /**
     * bindOne关联模型
     */
    public static <T> Handler<T> withOne(List<T> list, String propertyName) {
        ListBindOneHandler<T> tWithRelations = new ListBindOneHandler<>(list);
        return tWithRelations.bind(propertyName);
    }

    /**
     * bindOne关联模型
     */
    public static <T> Handler<T> withOne(List<T> list) {
        return new ListBindOneHandler<>(list);
    }

    // <========================================================

    /**
     * bindOne关联模型
     */
    public static <T> Handler<T> withOne(T entity, IGetter<T, ?> propertyGetter) {
        String property = BeanUtils.convertToFieldName(propertyGetter);
        return withOne(entity, property);
    }

    /**
     * bindOne关联模型
     */
    public static <T> Handler<T> withOne(T entity, String propertyName) {
        EntityBindOneHandler<T> tWithRelations = new EntityBindOneHandler<>(entity);
        return tWithRelations.bind(propertyName);
    }

    /**
     * bindOne关联模型
     */
    public static <T> Handler<T> withOne(T entity) {
        return new EntityBindOneHandler<>(entity);
    }
}
