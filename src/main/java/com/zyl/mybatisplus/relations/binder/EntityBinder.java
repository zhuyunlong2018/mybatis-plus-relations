package com.zyl.mybatisplus.relations.binder;

import com.zyl.mybatisplus.relations.func.IGetter;
import com.zyl.mybatisplus.relations.handler.EntityBindManyHandler;
import com.zyl.mybatisplus.relations.handler.EntityBindOneHandler;
import com.zyl.mybatisplus.relations.handler.Handler;


import java.util.List;

public class EntityBinder<T> extends Binder<T> {

    private final T entity;

    public EntityBinder(T entity) {
        this.entity = entity;
    }

    // 实体 绑定一对多
    public <R> Handler<T, R> bindMany(IGetter<T, List<R>> propertyGetter) {
        EntityBindManyHandler<T, R> handler = new EntityBindManyHandler<>(entity, this, propertyGetter);
        handlers.add(handler);
        return handler;
    }

    // 实体 绑定一对一
    public <R> Handler<T, R> bindOne(IGetter<T, R> propertyGetter) {
        EntityBindOneHandler<T, R> handler = new EntityBindOneHandler<>(entity, this, propertyGetter);
        handlers.add(handler);
        return handler;
    }
}
