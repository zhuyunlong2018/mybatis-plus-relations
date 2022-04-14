package com.zyl.mybatisplus.relations.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zyl.mybatisplus.relations.RelationCache;
import com.zyl.mybatisplus.relations.func.IGetter;
import com.zyl.mybatisplus.relations.utils.BeanUtils;

import java.util.List;
import java.util.function.Consumer;

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

    /**
     * 绑定关联表多条数据一对多
     *
     * @param propertyGetter
     * @param <R>
     * @return
     */
    public <R> void bind(IGetter<T, List<R>> propertyGetter) {
        String propertyName = BeanUtils.convertToFieldName(propertyGetter);
        bind(propertyName);
    }

    public <R> void bind(IGetter<T, List<R>> propertyGetter,
                         Consumer<LambdaQueryWrapper<R>> lambdaWrapperFunc) {
        String propertyName = BeanUtils.convertToFieldName(propertyGetter);
        bind(propertyName, lambdaWrapperFunc);
    }
}
