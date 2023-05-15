package com.gitee.zhuyunlong2018.mybatisplusrelations.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.zhuyunlong2018.mybatisplusrelations.cache.RelationCache;
import com.gitee.zhuyunlong2018.mybatisplusrelations.binder.Binder;
import com.gitee.zhuyunlong2018.mybatisplusrelations.utils.StringUtils;


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
