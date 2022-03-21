package com.zyl.mybatisplus.relations.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zyl.mybatisplus.relations.RelationCache;

import java.util.HashMap;
import java.util.List;

import static java.util.stream.Collectors.groupingBy;

public class ListBindManyHandler<T> extends ListHandler<T> {

    public ListBindManyHandler(List<T> list) {
        super(list);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    protected void queryRelation(RelationCache cache, LambdaQueryWrapper wrapper) {
        HashMap collect =
                (HashMap) (getForeignModel(cache)).selectList(wrapper).stream().collect(groupingBy(cache.getForeignPropertyGetter()));
        list.forEach(e -> cache.getRelationEntitySetter().accept(e,
                collect.get(cache.getLocalPropertyGetter().apply(e))));
    }

}
