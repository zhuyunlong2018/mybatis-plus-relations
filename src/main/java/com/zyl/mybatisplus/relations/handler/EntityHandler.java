package com.zyl.mybatisplus.relations.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zyl.mybatisplus.relations.RelationCache;
import com.zyl.mybatisplus.relations.Relations;

public abstract class EntityHandler<T> extends Handler<T>{
    /**
     * 关联主表entity
     */
    protected T entity;

    public EntityHandler(T entity) {
        this.entity = entity;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    protected LambdaQueryWrapper getWrapper(RelationCache cache) {
        // 用批量Id查询用户信息
        return Wrappers.lambdaQuery(cache.getForeignEntityClass()).eq(cache.getForeignPropertyGetter(),
                cache.getLocalPropertyGetter().apply(entity));
    }

    @Override
    protected RelationCache getRelationCache(String propertyName) {
        // 进行注入
        Class<?> entityClass = entity.getClass();
        if (!Relations.relationMap.containsKey(Relations.cacheKey(entityClass, propertyName))) {
            // 找不到关系
            return null;
        }
        // 进行注入
        return Relations.relationMap.get(Relations.cacheKey(entityClass, propertyName));
    }
}
