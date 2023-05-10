package com.zyl.mybatisplus.relations.binder;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.zyl.mybatisplus.relations.RelationCache;
import com.zyl.mybatisplus.relations.func.IGetter;
import com.zyl.mybatisplus.relations.handler.Handler;
import com.zyl.mybatisplus.relations.handler.IHandler;
import com.zyl.mybatisplus.relations.handler.UselessHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 绑定器，绑定主表关联模型和绑定副表关联处理器
 *
 * @param <T>
 */
public abstract class Binder<T> implements IBinder<T> {

    /**
     * 存储所有处理器，当某个处理器调用end方法或绑定器调用end方法时，统一进行sql检索关联绑定
     */
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
     * 是否无用的binder，当主表List为空，或者entity为null的时候，绑定的是无用处理器，不进行操作
     */
    protected Boolean useLess = true;

    /**
     * 初始化cache
     */
    protected abstract void init(IGetter<T, ?> propertyGetter);

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

    /**
     * 返回无用处理器
     *
     * @param <R>
     * @return
     */
    protected <R extends Model<?>> IHandler<T, R> useLessBinder() {
        return new UselessHandler<>(this);
    }
}
