package com.zyl.mybatisplus.relations.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zyl.mybatisplus.relations.RelationCache;
import com.zyl.mybatisplus.relations.Relations;
import com.zyl.mybatisplus.relations.binder.Binder;
import com.zyl.mybatisplus.relations.utils.StringUtils;

import java.util.function.Consumer;

public abstract class EntityHandler<T, R> extends Handler<T, R> {
    /**
     * 关联主表entity
     */
    protected T entity;

    public EntityHandler(T entity, Binder<T> binder, RelationCache cache) {
        this.entity = entity;
        this.binder = binder;
        this.cache = cache;
    }

    @SuppressWarnings({"unchecked"})
    @Override
    protected LambdaQueryWrapper<R> getQueryWrapper() {
        // 用批量Id查询用户信息
        LambdaQueryWrapper<R> wrapper = Wrappers.lambdaQuery(cache.getForeignEntityClass())
                .eq(cache.getForeignPropertyGetter(), cache.getLocalPropertyGetter().apply(entity));
        wrapper.apply(!StringUtils.isEmpty(cache.getApplySql()), cache.getApplySql());
        if (lambdaWrapperFunc != null) {
            lambdaWrapperFunc.accept(wrapper);
        }
        wrapper.last(!StringUtils.isEmpty(cache.getLastSql()), cache.getLastSql());
        return wrapper;
    }
}
