package com.zyl.mybatisplus.relations.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.zyl.mybatisplus.relations.RelationCache;
import com.zyl.mybatisplus.relations.func.IGetter;
import com.zyl.mybatisplus.relations.utils.BeanUtils;

import java.util.List;
import java.util.function.Consumer;

public abstract class Handler<T> {

    /**
     * 从缓存获取关联信息
     * @param propertyName
     * @return
     */
    protected abstract RelationCache getRelationCache(String propertyName);

    /**
     * 获取关联表query wrapper
     * @param cache
     * @return
     */
    @SuppressWarnings({"rawtypes"})
    protected abstract LambdaQueryWrapper getWrapper(RelationCache cache);

    /**
     * 数据库查询被关联表数据
     * @param cache
     * @param wrapper
     */
    @SuppressWarnings({"rawtypes"})
    protected abstract void queryRelation(RelationCache cache, LambdaQueryWrapper wrapper);

    /**
     * 绑定关联表数据
     * @param propertyName
     * @return
     */
    @SuppressWarnings({"rawtypes"})
    public Handler<T> bind(String propertyName) {
        RelationCache cache = getRelationCache(propertyName);
        if (null == cache) {
            return this;
        }
        LambdaQueryWrapper wrapper = getWrapper(cache);
        queryRelation(cache, wrapper);
        return this;
    }

    /**
     * 绑定关联表属性，并接收一个lambda查询关联数据
     * @param propertyGetter
     * @param lambdaWrapperFunc
     * @param <R>
     * @return
     */
    @SuppressWarnings({"unchecked"})
    public <R> Handler<T> bind(IGetter<T, List<R>> propertyGetter,
                                     Consumer<LambdaQueryWrapper<R>> lambdaWrapperFunc) {
        String propertyName = BeanUtils.convertToFieldName(propertyGetter);
        RelationCache cache = getRelationCache(propertyName);
        if (null == cache) {
            return this;
        }
        LambdaQueryWrapper<R> wrapper = (LambdaQueryWrapper<R>)getWrapper(cache);
        lambdaWrapperFunc.accept(wrapper);
        queryRelation(cache, wrapper);
        return this;
    }

    /**
     * 绑定关联表数据
     * @param propertyGetter
     * @param <R>
     * @return
     */
    public <R> Handler<T> bind(IGetter<T, List<R>> propertyGetter) {
        String propertyName = BeanUtils.convertToFieldName(propertyGetter);
        return bind(propertyName);
    }

    /**
     * 获取被关联表的entity实例
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
}
