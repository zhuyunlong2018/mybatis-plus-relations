package com.zyl.mybatisplus.relations.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zyl.mybatisplus.relations.RelationCache;
import com.zyl.mybatisplus.relations.binder.Binder;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static java.util.stream.Collectors.groupingBy;

public class ListBindManyHandler<T, R> extends ListHandler<T, R> {

    public ListBindManyHandler(List<T> list, Binder<T> binder, RelationCache cache) {
        super(list, binder, cache);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    protected void queryRelation() {
        foreignEntityList = getForeignModel()
                .selectList((LambdaQueryWrapper) getQueryWrapper());
        foreignEntityList = covertListModelToVO(foreignEntityList);
        HashMap collect = (HashMap) foreignEntityList
                .stream()
                .collect(groupingBy(cache.getForeignPropertyGetter()));
        list.forEach(e -> {
            Object subList = collect.get(cache.getLocalPropertyGetter().apply(e));
            cache.getRelationEntitySetter()
                    .accept(e, null == subList ? Collections.emptyList() : subList);
        });
    }

}
