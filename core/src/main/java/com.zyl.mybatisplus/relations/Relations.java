package com.zyl.mybatisplus.relations;

import com.zyl.mybatisplus.relations.binder.EntityBinder;
import com.zyl.mybatisplus.relations.binder.IBinder;
import com.zyl.mybatisplus.relations.binder.ListBinder;

import java.util.List;

public class Relations {

    /**
     * 获取实体类关联绑定类
     * @param entity
     * @param <T>
     * @return
     */
    public static <T> IBinder<T> with(T entity) {
        return new EntityBinder<>(entity);
    }

    /**
     * 获取列表关联绑定类
     * @param list
     * @param <T>
     * @return
     */
    public static <T> IBinder<T> with(List<T> list) {
        return new ListBinder<>(list);
    }
}
