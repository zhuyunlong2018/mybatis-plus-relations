package com.zyl.mybatisplus.relations.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zyl.mybatisplus.relations.RelationCache;
import com.zyl.mybatisplus.relations.binder.Binder;
import com.zyl.mybatisplus.relations.func.IGetter;
import com.zyl.mybatisplus.relations.utils.BeanUtils;

import java.util.List;
import java.util.function.Consumer;

public class EntityBindManyHandler<T, R> extends EntityHandler<T, R> {

    public EntityBindManyHandler(T entity, Binder<T> binder, IGetter<T, List<R>> propertyGetter) {
        super(entity, binder);
        String propertyName = BeanUtils.convertToFieldName(propertyGetter);
        bind(propertyName);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    protected void queryRelation(RelationCache cache, LambdaQueryWrapper wrapper) {
        List<Object> list = (List<Object>) (getForeignModel(cache)).selectList(wrapper);
        cache.getRelationEntitySetter().accept(entity, list);
    }
}
