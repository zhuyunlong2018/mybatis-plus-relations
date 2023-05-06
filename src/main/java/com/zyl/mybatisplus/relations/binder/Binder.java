package com.zyl.mybatisplus.relations.binder;
import com.zyl.mybatisplus.relations.func.IGetter;
import com.zyl.mybatisplus.relations.handler.Handler;

import java.util.ArrayList;
import java.util.List;

public abstract class Binder<T> {

    protected List<Handler<T, ?>> handlers = new ArrayList<>();

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
            handler.end();
        }
    }
}
