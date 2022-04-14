package com.zyl.mybatisplus.relations.handler;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zyl.mybatisplus.relations.RelationCache;
import com.zyl.mybatisplus.relations.Relations;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public abstract class ListHandler<T> extends Handler<T> {

    /**
     * 关联主表list
     */
    protected List<T> list;

    public ListHandler(List<T> list) {
        this.list = list;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    protected LambdaQueryWrapper getWrapper(RelationCache cache) {
        // 准备deptId方便批量查询用户信息
        Set<?> localProperties = list.stream()
                .map(s -> cache.getLocalPropertyGetter().apply(s)).collect(toSet());
        // 用批量Id查询用户信息
        return Wrappers.lambdaQuery(cache.getForeignEntityClass())
                .in(cache.getForeignPropertyGetter(), localProperties);
    }

    @Override
    protected RelationCache getRelationCache(String propertyName) {
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

}
