package com.zyl.mybatisplus.relations.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.zyl.mybatisplus.relations.RelationCache;
import com.zyl.mybatisplus.relations.Relations;
import com.zyl.mybatisplus.relations.binder.Binder;
import com.zyl.mybatisplus.relations.exceptions.RelationAnnotationException;
import com.zyl.mybatisplus.relations.func.IGetter;
import com.zyl.mybatisplus.relations.utils.BeanUtils;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class EntityBindManyHandler<T, R> extends EntityHandler<T, R> {

    protected List<R> foreignEntityList;

    public EntityBindManyHandler(T entity, Binder<T> binder, RelationCache cache) {
        super(entity, binder, cache);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    protected void queryRelation() {
        foreignEntityList = (List<R>) getForeignModel()
                .selectList((LambdaQueryWrapper) getQueryWrapper());
        foreignEntityList = covertListModelToVO(foreignEntityList);
        cache.getRelationEntitySetter().accept(entity, foreignEntityList);
    }

    /**
     * 深度绑定将提前触发end，所以需要放在最后
     *
     * @param binderConsumer
     * @return
     */
    public Binder<T> deepWith(Consumer<Binder<R>> binderConsumer) {
        end();
        binderConsumer.accept(Relations.with(foreignEntityList));
        return binder;
    }
}
