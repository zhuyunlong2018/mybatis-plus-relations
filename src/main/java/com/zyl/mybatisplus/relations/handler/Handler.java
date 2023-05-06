package com.zyl.mybatisplus.relations.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.zyl.mybatisplus.relations.RelationCache;
import com.zyl.mybatisplus.relations.Relations;
import com.zyl.mybatisplus.relations.binder.Binder;
import com.zyl.mybatisplus.relations.utils.StringUtils;

import java.util.function.Consumer;

public abstract class Handler<T, R> {

    protected Class<?> localEntityClass = void.class;

    /**
     * 关系构造器
     */
    protected Binder<T> binder;

    /**
     * 关联查询器
     */
    protected LambdaQueryWrapper wrapper;

    /**
     * 关联注解关系缓存
     */
    protected RelationCache cache;

    /**
     * 从缓存获取关联信息
     *
     * @param propertyName
     * @return
     */
    protected RelationCache getRelationCache(String propertyName) {
        if (!Relations.relationMap.containsKey(Relations.cacheKey(localEntityClass, propertyName))) {
            // 找不到关系
            return null;
        }
        // 进行注入
        return Relations.relationMap.get(Relations.cacheKey(localEntityClass, propertyName));
    }

    /**
     * 获取关联表query wrapper
     *
     * @param cache
     * @return
     */
    @SuppressWarnings({"rawtypes"})
    protected abstract LambdaQueryWrapper getWrapper(RelationCache cache);

    /**
     * 数据库查询被关联表数据
     *
     * @param cache
     * @param wrapper
     */
    @SuppressWarnings({"rawtypes"})
    protected abstract void queryRelation(RelationCache cache, LambdaQueryWrapper wrapper);

    /**
     * 绑定关联表数据
     *
     * @param propertyName
     */
    @SuppressWarnings({"rawtypes"})
    protected void bind(String propertyName) {
        cache = getRelationCache(propertyName);
        if (null == cache) {
            return;
        }
        wrapper = getWrapper(cache);
        wrapper.apply(!StringUtils.isEmpty(cache.getApplySql()), cache.getApplySql());
    }

    @SuppressWarnings({"unchecked"})
    public Handler<T, R> query(Consumer<LambdaQueryWrapper<R>> lambdaWrapperFunc) {
        lambdaWrapperFunc.accept(wrapper);
        return this;
    }

    /**
     * 获取被关联表的entity实例
     *
     * @param cache
     * @return
     */
    protected Model<?> getForeignModel(RelationCache cache) {
        Model<?> model = null;
        try {
            model = cache.getForeignEntityClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        assert model != null;
        return model;
    }

    /**
     * 如果需要绑定多个关联属性，这返回binder
     *
     * @return
     */
    public Binder<T> binder() {
        return binder;
    }

    /**
     * 结束关联查询，进行查询
     */
    public void end() {
        wrapper.last(!StringUtils.isEmpty(cache.getLastSql()), cache.getLastSql());
        queryRelation(cache, wrapper);
    }
}
