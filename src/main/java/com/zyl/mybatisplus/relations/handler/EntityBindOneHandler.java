package com.zyl.mybatisplus.relations.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zyl.mybatisplus.relations.RelationCache;
import com.zyl.mybatisplus.relations.binder.Binder;
import com.zyl.mybatisplus.relations.func.IGetter;
import com.zyl.mybatisplus.relations.utils.BeanUtils;
import com.zyl.mybatisplus.relations.utils.StringUtils;


public class EntityBindOneHandler<T, R> extends EntityHandler<T, R> {

    public EntityBindOneHandler(T entity, Binder<T> binder, IGetter<T, R> propertyGetter) {
        super(entity, binder);
        String propertyName = BeanUtils.convertToFieldName(propertyGetter);
        bind(propertyName);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    protected void queryRelation(RelationCache cache, LambdaQueryWrapper wrapper) {
        Object obj = (getForeignModel(cache)).selectOne(wrapper);
        cache.getRelationEntitySetter().accept(entity, obj);
    }
}
