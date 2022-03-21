package com.zyl.mybatisplus.relations.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.zyl.mybatisplus.relations.RelationCache;
import com.zyl.mybatisplus.relations.Relations;
import com.zyl.mybatisplus.relations.func.IGetter;
import com.zyl.mybatisplus.relations.utils.BeanUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;

public class EntityBindManyHandler<T> {

    protected T entity;

    public EntityBindManyHandler(T entity) {
        this.entity = entity;
    }


    @SuppressWarnings({"rawtypes", "unchecked"})
    private RelationCache getRelationCache(String propertyName) {
        // 进行注入
        Class<?> entityClass = entity.getClass();
        if (!Relations.relationMap.containsKey(Relations.cacheKey(entityClass, propertyName))) {
            // 找不到关系
            return null;
        }
        // 进行注入
        return Relations.relationMap.get(Relations.cacheKey(entityClass, propertyName));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private LambdaQueryWrapper getWrapper(RelationCache cache) {
        // 用批量Id查询用户信息
        return Wrappers.lambdaQuery(cache.getForeignEntityClass()).eq(cache.getForeignPropertyGetter(),
                cache.getLocalPropertyGetter().apply(entity));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void bindMany(RelationCache cache, LambdaQueryWrapper wrapper) {
        Model<?> model = null;
        try {
            model = cache.getForeignEntityClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        assert model != null;
        List<Object> list = (List<Object>) (model).selectList(wrapper);
        cache.getRelationEntitySetter().accept(entity, list);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public EntityBindManyHandler<T> bind(String propertyName) {
        RelationCache cache = getRelationCache(propertyName);
        if (null == cache) {
            return this;
        }
        LambdaQueryWrapper wrapper = getWrapper(cache);
        bindMany(cache, wrapper);
        return this;
    }

    public <R> EntityBindManyHandler<T> bind(IGetter<T, List<R>> propertyGetter) {
        String propertyName = BeanUtils.convertToFieldName(propertyGetter);
        return bind(propertyName);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public <R> EntityBindManyHandler<T> bind(IGetter<T, List<R>> propertyGetter,
                                           Consumer<LambdaQueryWrapper<R>> lambdaWrapperFunc) {
        String propertyName = BeanUtils.convertToFieldName(propertyGetter);
        RelationCache cache = getRelationCache(propertyName);
        if (null == cache) {
            return this;
        }
        LambdaQueryWrapper<R> wrapper = (LambdaQueryWrapper<R>)getWrapper(cache);
        lambdaWrapperFunc.accept(wrapper);
        bindMany(cache, wrapper);
        return this;
    }
}
