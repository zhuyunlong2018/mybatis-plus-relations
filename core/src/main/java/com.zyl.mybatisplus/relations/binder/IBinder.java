package com.zyl.mybatisplus.relations.binder;

import com.zyl.mybatisplus.relations.func.IGetter;
import com.zyl.mybatisplus.relations.handler.IHandler;
import com.zyl.mybatisplus.relations.handler.IManyBindHandler;

import java.util.List;

/**
 * 绑定器，绑定主表关联模型和绑定副表关联处理器
 *
 * @param <T>
 */
public interface IBinder<T> {

    /**
     * 绑定多对多
     */
    <R> IManyBindHandler<T, R> manyBindMany(IGetter<T, List<R>> propertyGetter);

    /**
     * 绑定一对多
     */
    <R> IHandler<T, R> bindMany(IGetter<T, List<R>> propertyGetter);

    /**
     * 绑定一对一
     */
    <R> IHandler<T, R> bindOne(IGetter<T, R> propertyGetter);

    /**
     * 最终进行汇总进行执行sql语句
     */
    void end();
}
