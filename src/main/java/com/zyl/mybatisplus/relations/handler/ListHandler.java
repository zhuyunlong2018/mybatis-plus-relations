package com.zyl.mybatisplus.relations.handler;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zyl.mybatisplus.relations.RelationCache;
import com.zyl.mybatisplus.relations.binder.Binder;
import com.zyl.mybatisplus.relations.exceptions.RelationAnnotationException;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public abstract class ListHandler<T, R> extends Handler<T, R> {

    /**
     * 关联主表list
     */
    protected List<T> list;

    public ListHandler(List<T> list, Binder<T> binder) {
        if (null == list) {
            throw new RelationAnnotationException("传入的list错误");
        }
        this.list = list;
        this.binder = binder;
        if (!this.list.isEmpty()) {
            T entityFirst = list.get(0);
            localEntityClass = entityFirst.getClass();
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    protected LambdaQueryWrapper getWrapper(RelationCache cache) {
        // 准备deptId方便批量查询用户信息
        Set<?> localProperties = list.stream()
                .map(s -> cache.getLocalPropertyGetter().apply(s)).collect(toSet());
        // 用批量Id查询用户信息
        return Wrappers.lambdaQuery(cache.getForeignEntityClass())
                .in(cache.getForeignPropertyGetter(), localProperties);
    }

}
