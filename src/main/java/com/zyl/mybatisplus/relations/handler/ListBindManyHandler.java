package com.zyl.mybatisplus.relations.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zyl.mybatisplus.relations.RelationCache;
import com.zyl.mybatisplus.relations.binder.Binder;
import com.zyl.mybatisplus.relations.func.IGetter;
import com.zyl.mybatisplus.relations.utils.BeanUtils;
import com.zyl.mybatisplus.relations.utils.StringUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

import static java.util.stream.Collectors.groupingBy;

public class ListBindManyHandler<T, R> extends ListHandler<T, R> {

    public ListBindManyHandler(List<T> list, Binder<T> binder, IGetter<T, List<R>> propertyGetter) {
        super(list, binder);
        String propertyName = BeanUtils.convertToFieldName(propertyGetter);
        bind(propertyName);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    protected void queryRelation(RelationCache cache, LambdaQueryWrapper wrapper) {
        HashMap collect = (HashMap) (getForeignModel(cache))
                .selectList(wrapper).stream()
                .collect(groupingBy(cache.getForeignPropertyGetter()));
        list.forEach(e -> {
            Object list = collect.get(cache.getLocalPropertyGetter().apply(e));
            cache.getRelationEntitySetter()
                    .accept(e, null == list ? Collections.emptyList() : list);
        });
    }

}
