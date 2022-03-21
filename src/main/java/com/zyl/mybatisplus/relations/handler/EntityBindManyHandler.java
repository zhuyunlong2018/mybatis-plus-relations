package com.zyl.mybatisplus.relations.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zyl.mybatisplus.relations.RelationCache;

import java.util.List;

public class EntityBindManyHandler<T> extends EntityHandler<T> {

    public EntityBindManyHandler(T entity) {
        super(entity);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    protected void queryRelation(RelationCache cache, LambdaQueryWrapper wrapper) {
        List<Object> list = (List<Object>) (getForeignModel(cache)).selectList(wrapper);
        cache.getRelationEntitySetter().accept(entity, list);
    }
}
