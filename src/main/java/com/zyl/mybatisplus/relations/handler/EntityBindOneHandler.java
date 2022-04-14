package com.zyl.mybatisplus.relations.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zyl.mybatisplus.relations.RelationCache;
import com.zyl.mybatisplus.relations.func.IGetter;
import com.zyl.mybatisplus.relations.utils.BeanUtils;

import java.util.function.Consumer;

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

    public <R> void bind(IGetter<T, R> propertyGetter) {
        String propertyName = BeanUtils.convertToFieldName(propertyGetter);
        bind(propertyName);
    }

    public <R> void bind(IGetter<T, R> propertyGetter,
                                  Consumer<LambdaQueryWrapper<R>> lambdaWrapperFunc) {
        String propertyName = BeanUtils.convertToFieldName(propertyGetter);
        bind(propertyName, lambdaWrapperFunc);
    }
}
