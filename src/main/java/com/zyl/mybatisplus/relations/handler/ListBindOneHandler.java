package com.zyl.mybatisplus.relations.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zyl.mybatisplus.relations.RelationCache;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


public class ListBindOneHandler<T> extends ListHandler<T> {

    public ListBindOneHandler(List<T> list) {
        super(list);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    protected void queryRelation(RelationCache cache, LambdaQueryWrapper wrapper) {
        HashMap collect =
                (HashMap) (getForeignModel(cache)).selectList(wrapper).stream().collect(Collectors.toMap(cache.getForeignPropertyGetter(), item -> item));
        System.out.println(collect);
        list.forEach(e -> cache.getRelationEntitySetter().accept(e,
                collect.get(cache.getLocalPropertyGetter().apply(e))));
    }


}
