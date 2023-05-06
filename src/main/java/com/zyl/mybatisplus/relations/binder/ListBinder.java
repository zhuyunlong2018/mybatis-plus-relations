package com.zyl.mybatisplus.relations.binder;

import com.zyl.mybatisplus.relations.func.IGetter;
import com.zyl.mybatisplus.relations.handler.Handler;
import com.zyl.mybatisplus.relations.handler.ListBindManyHandler;
import com.zyl.mybatisplus.relations.handler.ListBindOneHandler;

import java.util.List;

public class ListBinder<T> extends Binder<T> {

    private final List<T> list;


    public ListBinder(List<T> list) {
        this.list = list;
    }

    // 列表 绑定一对多
    public <R> Handler<T, R> bindMany(IGetter<T, List<R>> propertyGetter) {
        ListBindManyHandler<T, R> handler = new ListBindManyHandler<>(list, this, propertyGetter);
        handlers.add(handler);
        return handler;
    }

    // 列表 绑定一对一
    public <R> Handler<T, R> bindOne(IGetter<T, R> propertyGetter) {
        ListBindOneHandler<T, R> handler = new ListBindOneHandler<>(list, this, propertyGetter);
        handlers.add(handler);
        return handler;
    }

}
