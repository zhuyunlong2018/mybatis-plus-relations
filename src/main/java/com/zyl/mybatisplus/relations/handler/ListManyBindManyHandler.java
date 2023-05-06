package com.zyl.mybatisplus.relations.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.zyl.mybatisplus.relations.RelationCache;
import com.zyl.mybatisplus.relations.binder.Binder;
import com.zyl.mybatisplus.relations.func.IGetter;
import com.zyl.mybatisplus.relations.utils.BeanUtils;
import com.zyl.mybatisplus.relations.utils.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;

public class ListManyBindManyHandler<T, R> extends ListHandler<T, R> {

    public ListManyBindManyHandler(List<T> list, Binder<T> binder, IGetter<T, List<R>> propertyGetter) {
        super(list, binder);
        String propertyName = BeanUtils.convertToFieldName(propertyGetter);
        bind(propertyName);
    }

    /**
     * 绑定关联表数据
     *
     * @param propertyName
     */
    @SuppressWarnings({"rawtypes"})
    @Override
    protected void bind(String propertyName) {
        cache = getRelationCache(propertyName);
        if (null == cache) {
            return;
        }
        wrapper = getWrapper(cache);
        wrapper.apply(!StringUtils.isEmpty(cache.getLinkApplySql()), cache.getLinkApplySql());
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    protected LambdaQueryWrapper getWrapper(RelationCache cache) {
        // 准备deptId方便批量查询用户信息
        Set<?> localProperties = list.stream()
                .map(s -> cache.getLocalPropertyGetter().apply(s)).collect(toSet());
        // 用批量Id查询用户信息
        return Wrappers.lambdaQuery(cache.getLinkEntityClass())
                .in(cache.getLinkLocalPropertyGetter(), localProperties);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    protected void queryRelation(RelationCache cache, LambdaQueryWrapper wrapper) {
        Set<?> localProperties = list.stream()
                .map(s -> cache.getLocalPropertyGetter().apply(s)).collect(toSet());
        Model<?> linkModel = null;
        try {
            linkModel = cache.getLinkEntityClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        // 中间表集合
        assert linkModel != null;
        List linkList = linkModel.selectList(wrapper);

        // 外键集合ID
        Set<?> linkForeignProperties = (Set<?>) linkList
                .stream()
                .map(cache.getLinkForeignPropertyGetter())
                .collect(Collectors.toSet());
        LambdaQueryWrapper foreignWrapper = Wrappers.lambdaQuery(cache.getForeignEntityClass())
                .in(cache.getForeignPropertyGetter(), linkForeignProperties);
        foreignWrapper.apply(!StringUtils.isEmpty(cache.getApplySql()), cache.getApplySql());
        if (localProperties.size() > 0 && linkForeignProperties.size() > 0) {
            // 中间表hashMap
            HashMap linkMap = (HashMap) linkList.stream()
                    .collect(groupingBy(cache.getLinkLocalPropertyGetter()));
            // 关联表hashMap
            HashMap collect = (HashMap) (getForeignModel(cache))
                    .selectList(foreignWrapper).stream()
                    .collect(groupingBy(cache.getForeignPropertyGetter()));
            // 组装
            list.forEach(e -> {
                Object linkData = linkMap.get(cache.getLocalPropertyGetter().apply(e));
                Object list = collect.get(cache.getLinkForeignPropertyGetter().apply(linkData));
                cache.getRelationEntitySetter()
                        .accept(e, null == list ? Collections.emptyList() : list);
            });
        }
    }

}
