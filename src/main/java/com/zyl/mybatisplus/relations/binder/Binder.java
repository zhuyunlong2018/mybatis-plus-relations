package com.zyl.mybatisplus.relations.binder;

import com.zyl.mybatisplus.relations.RelationCache;
import com.zyl.mybatisplus.relations.func.IGetter;
import com.zyl.mybatisplus.relations.handler.Handler;
import com.zyl.mybatisplus.relations.handler.UselessHandler;

import java.util.ArrayList;
import java.util.List;

public abstract class Binder<T> {

    protected List<Handler<T, ?>> handlers = new ArrayList<>();

    /**
     * 主表model类
     */
    protected Class<?> localEntityClass = void.class;

    /**
     * 关联缓存器
     */
    protected RelationCache cache;

    /**
     * 是否无用的binder
     */
    protected Boolean useLess = true;

    /**
     * 初始化cache
     */
    protected abstract void init(IGetter<T, ?> propertyGetter);

    /**
     * 绑定多对多
     *
     * @param propertyGetter
     * @return
     */
    public abstract <R> Handler<T, R> manyBindMany(IGetter<T, List<R>> propertyGetter);

    /**
     * 绑定一对多
     *
     * @param propertyGetter
     * @return
     */
    public abstract <R> Handler<T, R> bindMany(IGetter<T, List<R>> propertyGetter);

    /**
     * 绑定一对一
     *
     * @param propertyGetter
     * @return
     */
    public abstract <R> Handler<T, R> bindOne(IGetter<T, R> propertyGetter);

    /**
     * 最终进行汇总进行执行sql语句
     */
    public void end() {
        for (Handler<T, ?> handler : handlers) {
            if (!handler.getEnded()) {
                handler.end();
            }
        }
    }

    protected <R> Handler<T, R> useLessBinder() {
        return new UselessHandler<>();
    }
}
