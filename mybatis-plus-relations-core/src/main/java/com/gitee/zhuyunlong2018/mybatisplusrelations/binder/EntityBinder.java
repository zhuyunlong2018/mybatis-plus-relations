package com.gitee.zhuyunlong2018.mybatisplusrelations.binder;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.gitee.zhuyunlong2018.mybatisplusrelations.cache.RelationCache;
import com.gitee.zhuyunlong2018.mybatisplusrelations.handler.*;
import com.gitee.zhuyunlong2018.mybatisplusrelations.func.IGetter;
import com.gitee.zhuyunlong2018.mybatisplusrelations.utils.BeanUtils;


import java.util.List;

/**
 * 主表单独entity绑定器
 *
 * @param <T>
 */
public class EntityBinder<T> extends Binder<T> {

    private final T entity;

    public EntityBinder(T entity) {
        this.entity = entity;
    }

    @Override
    protected void init(IGetter<T, ?> propertyGetter) {
        if (null != entity) {
            this.localEntityClass = (Class<T>) entity.getClass();
            String propertyName = BeanUtils.convertToFieldName(propertyGetter);
            cache = RelationCache.getCache(localEntityClass, propertyName);
            cache.setFieldName(propertyName);
            if (null != cache.getLocalPropertyGetter().apply(entity)) {
                useLess = false;
            }
        }
    }

    /**
     * 绑定多对多关系
     *
     * @param propertyGetter
     * @param <R>
     * @return
     */
    @Override
    public <R extends Model<?>> IManyBindHandler<T, R> manyBindMany(IGetter<T, List<R>> propertyGetter) {
        init(propertyGetter);
        if (useLess) {
            return (IManyBindHandler<T, R>) useLessBinder();
        }
        EntityManyBindManyHandler<T, R> handler = new EntityManyBindManyHandler<>(entity, this, cache);
        handlers.add(handler);
        return handler;
    }

    /**
     * 绑定一对多关系
     *
     * @param propertyGetter
     * @param <R>
     * @return
     */
    @Override
    public <R extends Model<?>> IHandler<T, R> bindMany(IGetter<T, List<R>> propertyGetter) {
        init(propertyGetter);
        if (useLess) {
            return useLessBinder();
        }
        EntityBindManyHandler<T, R> handler = new EntityBindManyHandler<>(entity, this, cache);
        handlers.add(handler);
        return handler;
    }

    /**
     * 绑定一对一关系
     *
     * @param propertyGetter
     * @param <R>
     * @return
     */
    @Override
    public <R extends Model<?>> IHandler<T, R> bindOne(IGetter<T, R> propertyGetter) {
        init(propertyGetter);
        if (useLess) {
            return useLessBinder();
        }
        EntityBindOneHandler<T, R> handler = new EntityBindOneHandler<>(entity, this, cache);
        handlers.add(handler);
        return handler;
    }
}
