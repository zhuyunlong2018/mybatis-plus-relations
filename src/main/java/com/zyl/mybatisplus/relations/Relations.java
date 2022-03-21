package com.zyl.mybatisplus.relations;

import com.zyl.mybatisplus.relations.func.IGetter;
import com.zyl.mybatisplus.relations.handler.EntityBindManyHandler;
import com.zyl.mybatisplus.relations.handler.ListBindManyHandler;
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

    public static <T> List<T> with(List<T> list, String... propertyNames) {
        if (list.size() > 0) {
            for (String propertyName : propertyNames) {
                // 进行注入
                with(list, propertyName);
            }
        }
        return list;
    }

    /**
     * 关联模型，使用lambda getter方法
     *
     * @param list
     * @param propertyGetter
     * @param <T>
     * @return
     */
    public static <T> ListBindManyHandler<T> with(List<T> list, IGetter<T, ?> propertyGetter) {
        String property = BeanUtils.convertToFieldName(propertyGetter);
        return with(list, property);
    }

    /**
     * 关联模型，使用属性字符串
     *
     * @param list
     * @param propertyName
     * @param <T>
     * @return
     */
    public static <T> ListBindManyHandler<T> with(List<T> list, String propertyName) {
        ListBindManyHandler<T> tWithRelations = new ListBindManyHandler<>(list);
        return tWithRelations.bind(propertyName);
    }

    public static <T> ListBindManyHandler<T> with(List<T> list) {
        return new ListBindManyHandler<>(list);
    }


    public static <T> EntityBindManyHandler<T> with(T entity, IGetter<T, ?> propertyGetter) {
        String property = BeanUtils.convertToFieldName(propertyGetter);
        return with(entity, property);
    }

    public static <T> EntityBindManyHandler<T> with(T entity, String propertyName) {
        EntityBindManyHandler<T> tWithRelations = new EntityBindManyHandler<>(entity);
        return tWithRelations.bind(propertyName);
    }

    public static <T> EntityBindManyHandler<T> with(T entity) {
        return new EntityBindManyHandler<>(entity);
    }
}
