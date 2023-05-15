package com.gitee.zhuyunlong2018.mybatisplusrelations.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gitee.zhuyunlong2018.mybatisplusrelations.cache.RelationCache;
import com.gitee.zhuyunlong2018.mybatisplusrelations.binder.Binder;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


public class ListBindOneHandler<T, R> extends ListHandler<T, R> {

    public ListBindOneHandler(List<T> list, Binder<T> binder, RelationCache cache) {
        super(list, binder, cache);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    protected void queryRelation() {
        List list = getForeignModel()
                .selectList((LambdaQueryWrapper) getQueryWrapper());
        foreignEntityList = covertListModelToVO(list);
        HashMap collect = (HashMap) foreignEntityList
                .stream()
                .collect(Collectors.toMap(cache.getForeignPropertyGetter(), item -> item));
        this.list.forEach(e -> cache.getRelationEntitySetter()
                .accept(e, collect.get(cache.getLocalPropertyGetter().apply(e))));
    }

}
