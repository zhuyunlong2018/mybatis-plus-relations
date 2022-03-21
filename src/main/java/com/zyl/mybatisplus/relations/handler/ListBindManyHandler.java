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

public class ListBindManyHandler<T> {

    protected List<T> list;

    public ListBindManyHandler(List<T> list) {
        this.list = list;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private RelationCache getRelationCache(String propertyName) {
        if (list.size() == 0) {
            return null;
        }
        // 进行注入
        T entityFirst = list.get(0);
        Class<?> entityClass = entityFirst.getClass();
        if (!Relations.relationMap.containsKey(Relations.cacheKey(entityClass, propertyName))) {
            // 找不到关系
            return null;
        }
        // 进行注入
        return Relations.relationMap.get(Relations.cacheKey(entityClass, propertyName));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private LambdaQueryWrapper getWrapper(RelationCache cache) {
        // 准备deptId方便批量查询用户信息
        Set<?> localProperties = list.stream().map(s -> cache.getLocalPropertyGetter().apply(s)).collect(toSet());
        // 用批量Id查询用户信息
        return Wrappers.lambdaQuery(cache.getForeignEntityClass()).in(cache.getForeignPropertyGetter(),
                localProperties);
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
        HashMap collect =
                (HashMap) (model).selectList(wrapper).stream().collect(groupingBy(cache.getForeignPropertyGetter()));
        list.forEach(e -> cache.getRelationEntitySetter().accept(e,
                collect.get(cache.getLocalPropertyGetter().apply(e))));
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public ListBindManyHandler<T> bind(String propertyName) {
        RelationCache cache = getRelationCache(propertyName);
        if (null == cache) {
            return this;
        }
        LambdaQueryWrapper wrapper = getWrapper(cache);
        bindMany(cache, wrapper);
        return this;
    }

    public <R> ListBindManyHandler<T> bind(IGetter<T, List<R>> propertyGetter) {
        String propertyName = BeanUtils.convertToFieldName(propertyGetter);
        return bind(propertyName);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public <R> ListBindManyHandler<T> bind(IGetter<T, List<R>> propertyGetter,
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
