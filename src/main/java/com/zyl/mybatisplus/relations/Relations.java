package com.zyl.mybatisplus.relations;

import com.zyl.mybatisplus.relations.binder.Binder;
import com.zyl.mybatisplus.relations.binder.EntityBinder;
import com.zyl.mybatisplus.relations.binder.ListBinder;
import lombok.Data;

import java.util.List;

@Data
public class Relations {

    /**
     * 获取实体类关联绑定类
     * @param entity
     * @param <T>
     * @return
     */
    public static <T> Binder<T> with(T entity) {
        return new EntityBinder<>(entity);
    }

    /**
     * 获取列表关联绑定类
     * @param list
     * @param <T>
     * @return
     */
    public static <T> Binder<T> with(List<T> list) {
        return new ListBinder<>(list);
    }
}
