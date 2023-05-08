package com.zyl.mybatisplus.relations.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zyl.mybatisplus.relations.RelationCache;
import com.zyl.mybatisplus.relations.Relations;
import com.zyl.mybatisplus.relations.binder.Binder;

import java.util.function.Consumer;


public class EntityBindOneHandler<T, R> extends EntityHandler<T, R> {


    private R foreignEntity;

    public EntityBindOneHandler(T entity, Binder<T> binder, RelationCache cache) {
        super(entity, binder, cache);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    protected void queryRelation() {
        foreignEntity = (R) getForeignModel()
                .selectOne((LambdaQueryWrapper) getQueryWrapper());
        foreignEntity = covertVoToModel(foreignEntity);
        cache.getRelationEntitySetter().accept(entity, foreignEntity);
    }

    /**
     * 深度绑定将提前触发end，所以需要放在最后
     *
     * @param binderConsumer
     * @return
     */
    public Binder<T> deepWith(Consumer<Binder<R>> binderConsumer) {
        end();
        binderConsumer.accept(Relations.with(foreignEntity));
        return binder;
    }
}
