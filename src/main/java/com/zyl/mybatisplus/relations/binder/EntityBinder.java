package com.zyl.mybatisplus.relations.binder;

import com.zyl.mybatisplus.relations.RelationCache;
import com.zyl.mybatisplus.relations.func.IGetter;
import com.zyl.mybatisplus.relations.handler.EntityBindManyHandler;
import com.zyl.mybatisplus.relations.handler.EntityBindOneHandler;
import com.zyl.mybatisplus.relations.handler.EntityManyBindManyHandler;
import com.zyl.mybatisplus.relations.handler.Handler;
import com.zyl.mybatisplus.relations.utils.BeanUtils;


import java.util.List;

public class EntityBinder<T> extends Binder<T> {

    private final T entity;

    public EntityBinder(T entity) {
        this.entity = entity;
    }

    @Override
    protected void init(IGetter<T, ?> propertyGetter) {
        if (null != entity) {
            this.localEntityClass = (Class<T>) entity.getClass();
            String propertyName = BeanUtils.convertToFieldName(propertyGetter);
            cache = RelationCache.getCache(localEntityClass, propertyName);
            cache.setFieldName(propertyName);
            if (null != cache.getLocalPropertyGetter().apply(entity)) {
                useLess = false;
            }
        }
    }

    /**
     * 绑定多对多关系
     * @param propertyGetter
     * @param <R>
     * @return
     */
    @Override
    public <R> Handler<T, R> manyBindMany(IGetter<T, List<R>> propertyGetter) {
        init(propertyGetter);
        if (useLess) {
            return useLessBinder();
        }
        EntityManyBindManyHandler<T, R> handler = new EntityManyBindManyHandler<>(entity, this, cache);
        handlers.add(handler);
        return handler;
    }

    /**
     * 绑定一对多关系
     * @param propertyGetter
     * @param <R>
     * @return
     */
    @Override
    public <R> Handler<T, R> bindMany(IGetter<T, List<R>> propertyGetter) {
        init(propertyGetter);
        if (useLess) {
            return useLessBinder();
        }
        EntityBindManyHandler<T, R> handler = new EntityBindManyHandler<>(entity, this, cache);
        handlers.add(handler);
        return handler;
    }

    /**
     * 绑定一对一关系
     * @param propertyGetter
     * @param <R>
     * @return
     */
    @Override
    public <R> Handler<T, R> bindOne(IGetter<T, R> propertyGetter) {
        init(propertyGetter);
        if (useLess) {
            return useLessBinder();
        }
        EntityBindOneHandler<T, R> handler = new EntityBindOneHandler<>(entity, this, cache);
        handlers.add(handler);
        return handler;
    }
}
