package com.zyl.mybatisplus.relations.handler;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zyl.mybatisplus.relations.binder.Binder;

import java.util.function.Consumer;

/**
 * 无用的处理模块，当绑定对象list为empty或者entity为null的时候使用
 *
 * @param <T>
 * @param <R>
 */
public class UselessHandler<T, R> extends Handler<T, R> {
    @Override
    protected LambdaQueryWrapper<R> getQueryWrapper() {
        return null;
    }

    @Override
    protected void queryRelation() {
    }

    @Override
    public Handler<T, R> query(Consumer<LambdaQueryWrapper<R>> lambdaWrapperFunc) {
        return this;
    }

    @Override
    public Binder<T> deepWith(Consumer<Binder<R>> binderConsumer) {
        return binder;
    }

    @Override
    public void end() {}
}
