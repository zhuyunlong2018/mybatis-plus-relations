package com.zyl.mybatisplus.relations.binder;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zyl.mybatisplus.relations.func.IGetter;
import com.zyl.mybatisplus.relations.handler.EntityBindManyHandler;
import com.zyl.mybatisplus.relations.handler.EntityBindOneHandler;


import java.util.List;
import java.util.function.Consumer;

public class EntityBinder<T> extends Binder<T> {

    private final T entity;

    public EntityBinder(T entity) {
        this.entity = entity;
    }

    // 实体 绑定一对多
    public <R> Binder<T> bindMany(IGetter<T, List<R>> propertyGetter) {
        if (null == entity) {
            return this;
        }
        EntityBindManyHandler<T> handler = new EntityBindManyHandler<>(entity);
        handler.bind(propertyGetter);
        return this;
    }

    // 实体 绑定一对多 带条件过滤
    public <R> Binder<T> bindMany(IGetter<T, List<R>> propertyGetter,
                                  Consumer<LambdaQueryWrapper<R>> lambdaWrapperFunc) {
        if (null == entity) {
            return this;
        }
        EntityBindManyHandler<T> handler = new EntityBindManyHandler<>(entity);
        handler.bind(propertyGetter, lambdaWrapperFunc);
        return this;
    }

    // 实体 绑定一对一
    public <R> Binder<T> bindOne(IGetter<T, R> propertyGetter) {
        if (null == entity) {
            return this;
        }
        EntityBindOneHandler<T> handler = new EntityBindOneHandler<>(entity);
        handler.bind(propertyGetter);
        return this;
    }

    // 实体 绑定一对一 带条件过滤
    public <R> Binder<T> bindOne(IGetter<T, R> propertyGetter,
                                  Consumer<LambdaQueryWrapper<R>> lambdaWrapperFunc) {
        if (null == entity) {
            return this;
        }
        EntityBindOneHandler<T> handler = new EntityBindOneHandler<>(entity);
        handler.bind(propertyGetter, lambdaWrapperFunc);
        return this;
    }
}
