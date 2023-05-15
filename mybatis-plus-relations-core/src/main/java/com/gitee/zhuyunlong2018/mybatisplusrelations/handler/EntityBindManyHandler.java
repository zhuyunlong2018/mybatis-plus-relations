package com.gitee.zhuyunlong2018.mybatisplusrelations.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gitee.zhuyunlong2018.mybatisplusrelations.cache.RelationCache;
import com.gitee.zhuyunlong2018.mybatisplusrelations.Relations;
import com.gitee.zhuyunlong2018.mybatisplusrelations.binder.Binder;
import com.gitee.zhuyunlong2018.mybatisplusrelations.binder.IBinder;

import java.util.List;
import java.util.function.Consumer;

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
     * @return IBinder
     */
    public IBinder<T> deepWith(Consumer<IBinder<R>> binderConsumer) {
        end();
        binderConsumer.accept(Relations.with(foreignEntityList));
        return binder;
    }
}
