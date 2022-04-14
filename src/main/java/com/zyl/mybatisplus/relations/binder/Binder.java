package com.zyl.mybatisplus.relations.binder;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zyl.mybatisplus.relations.func.IGetter;

import java.util.List;
import java.util.function.Consumer;

public abstract class Binder<T> {

    /**
     * 绑定一对多
     * @param propertyGetter
     * @param <R>
     * @return
     */
    public abstract  <R> Binder<T> bindMany(IGetter<T, List<R>> propertyGetter);

    /**
     * 绑定一对多，待条件过滤
     * @param propertyGetter
     * @param lambdaWrapperFunc
     * @param <R>
     * @return
     */
    public abstract <R> Binder<T> bindMany(IGetter<T, List<R>> propertyGetter,
                                                  Consumer<LambdaQueryWrapper<R>> lambdaWrapperFunc);


    /**
     * 绑定一对一
     * @param propertyGetter
     * @param <R>
     * @return
     */
    public abstract  <R> Binder<T> bindOne(IGetter<T, R> propertyGetter);

    /**
     * 绑定一对一 带条件过滤
     * @param propertyGetter
     * @param lambdaWrapperFunc
     * @param <R>
     * @return
     */
    public abstract  <R> Binder<T> bindOne(IGetter<T, R> propertyGetter,
                                 Consumer<LambdaQueryWrapper<R>> lambdaWrapperFunc);
}
