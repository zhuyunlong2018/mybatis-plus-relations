package com.gitee.zhuyunlong2018.mybatisplusrelations.binder;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.gitee.zhuyunlong2018.mybatisplusrelations.cache.RelationCache;
import com.gitee.zhuyunlong2018.mybatisplusrelations.exceptions.RelationAnnotationException;
import com.gitee.zhuyunlong2018.mybatisplusrelations.handler.*;
import com.gitee.zhuyunlong2018.mybatisplusrelations.func.IGetter;
import com.gitee.zhuyunlong2018.mybatisplusrelations.utils.BeanUtils;

import java.util.List;

/**
 * 主表selectList的绑定器
 *
 * @param <T>
 */
public class ListBinder<T> extends Binder<T> {

    private final List<T> list;

    public ListBinder(List<T> list) {
        this.list = list;
    }

    @Override
    protected void init(IGetter<T, ?> propertyGetter) {
        if (null != list && !list.isEmpty()) {
            T entityFirst = list.get(0);
            localEntityClass = entityFirst.getClass();
            String propertyName = BeanUtils.convertToFieldName(propertyGetter);
            cache = RelationCache.getCache(localEntityClass, propertyName);
            if (cache.getLocalPropertyGetter().apply(entityFirst) != null) {
                // 只有list不为空，且list内的entity的关联属性不为空，才是有效的
                useLess = false;
            }
        }
    }

    /**
     * 多对多
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
        if (null == cache.getLinkEntityClass()) {
            throw new RelationAnnotationException("没有中间表无法绑定多对多");
        }
        ListManyBindManyHandler<T, R> handler = new ListManyBindManyHandler<>(list, this,
                cache);
        handlers.add(handler);
        return handler;
    }

    /**
     * 列表 绑定一对多
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
        ListBindManyHandler<T, R> handler = new ListBindManyHandler<>(list, this, cache);
        handlers.add(handler);
        return handler;
    }

    /**
     * 列表 绑定一对一
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
        ListBindOneHandler<T, R> handler = new ListBindOneHandler<>(list, this, cache);
        handlers.add(handler);
        return handler;
    }

}
