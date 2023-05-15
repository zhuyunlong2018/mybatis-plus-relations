package com.gitee.zhuyunlong2018.mybatisplusrelations.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gitee.zhuyunlong2018.mybatisplusrelations.cache.RelationCache;
import com.gitee.zhuyunlong2018.mybatisplusrelations.Relations;
import com.gitee.zhuyunlong2018.mybatisplusrelations.binder.IBinder;
import com.gitee.zhuyunlong2018.mybatisplusrelations.binder.Binder;

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
    public IBinder<T> deepWith(Consumer<IBinder<R>> binderConsumer) {
        end();
        binderConsumer.accept(Relations.with(foreignEntity));
        return binder;
    }
}
