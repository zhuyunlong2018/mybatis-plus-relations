package com.zyl.mybatisplus.relations.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zyl.mybatisplus.relations.RelationCache;

public class EntityBindOneHandler<T> extends EntityHandler<T>{

    public EntityBindOneHandler(T entity) {
        super(entity);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    protected void queryRelation(RelationCache cache, LambdaQueryWrapper wrapper) {
        Object obj = (getForeignModel(cache)).selectOne(wrapper);
        cache.getRelationEntitySetter().accept(entity, obj);
    }

}
