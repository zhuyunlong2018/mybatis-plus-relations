package com.zyl.mybatisplus.relations.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zyl.mybatisplus.relations.RelationCache;
import com.zyl.mybatisplus.relations.binder.Binder;
import com.zyl.mybatisplus.relations.exceptions.RelationAnnotationException;

public abstract class EntityHandler<T, R> extends Handler<T, R> {
    /**
     * 关联主表entity
     */
    protected T entity;

    public EntityHandler(T entity, Binder<T> binder) {
        if (null == entity) {
            throw new RelationAnnotationException("模型类错误");
        }
        this.entity = entity;
        this.binder = binder;
        this.localEntityClass = (Class<T>) entity.getClass();
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    protected LambdaQueryWrapper getWrapper(RelationCache cache) {
        // 用批量Id查询用户信息
        return Wrappers.lambdaQuery(cache.getForeignEntityClass())
                .eq(
                        cache.getForeignPropertyGetter(),
                        cache.getLocalPropertyGetter().apply(entity)
                );
    }
}
