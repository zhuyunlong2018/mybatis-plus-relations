package com.gitee.zhuyunlong2018.mybatisplusrelations.handler;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.gitee.zhuyunlong2018.mybatisplusrelations.binder.IBinder;

import java.util.function.Consumer;

/**
 * 无用的处理模块，当绑定对象list为empty或者entity为null的时候使用
 *
 * @param <T>
 * @param <R>
 */
public class UselessHandler<T, R> extends Handler<T, R> implements IManyBindHandler<T, R> {
    public UselessHandler(IBinder<T> binder) {
        this.binder = binder;
    }
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
    public IBinder<T> deepWith(Consumer<IBinder<R>> binderConsumer) {
        return binder;
    }

    @Override
    public void end() {}

    @Override
    public <L extends Model<?>> IManyBindHandler<T, R> linkQuery(Consumer<LambdaQueryWrapper<L>> lambdaWrapperFunc) {
        return this;
    }
}
