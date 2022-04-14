package com.zyl.mybatisplus.relations.binder;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zyl.mybatisplus.relations.func.IGetter;
import com.zyl.mybatisplus.relations.handler.ListBindManyHandler;
import com.zyl.mybatisplus.relations.handler.ListBindOneHandler;

import java.util.List;
import java.util.function.Consumer;

public class ListBinder<T> extends Binder<T> {

    private final List<T> list;


    public ListBinder(List<T> list) {
        this.list = list;
    }

    // 列表 绑定一对多
    public <R> Binder<T> bindMany(IGetter<T, List<R>> propertyGetter) {
        if (list.size() == 0) {
            return this;
        }
        ListBindManyHandler<T> handler = new ListBindManyHandler<T>(list);
        handler.bind(propertyGetter);
        return this;
    }

    // 列表 绑定一对多 带条件过滤
    public <R> Binder<T> bindMany(IGetter<T, List<R>> propertyGetter,
                                  Consumer<LambdaQueryWrapper<R>> lambdaWrapperFunc) {
        if (list.size() == 0) {
            return this;
        }
        ListBindManyHandler<T> handler = new ListBindManyHandler<T>(list);
        handler.bind(propertyGetter, lambdaWrapperFunc);
        return this;
    }

    // 列表 绑定一对一
    public <R> Binder<T> bindOne(IGetter<T, R> propertyGetter) {
        if (list.size() == 0) {
            return this;
        }
        ListBindOneHandler<T> handler = new ListBindOneHandler<>(list);
        handler.bind(propertyGetter);
        return this;
    }

    // 列表 绑定一对一 带条件过滤
    public <R> Binder<T> bindOne(IGetter<T, R> propertyGetter,
                                  Consumer<LambdaQueryWrapper<R>> lambdaWrapperFunc) {
        if (list.size() == 0) {
            return this;
        }
        ListBindOneHandler<T> handler = new ListBindOneHandler<>(list);
        handler.bind(propertyGetter, lambdaWrapperFunc);
        return this;
    }
}
