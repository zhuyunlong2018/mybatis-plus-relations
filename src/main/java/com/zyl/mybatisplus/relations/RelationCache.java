package com.zyl.mybatisplus.relations;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.util.function.BiConsumer;
import java.util.function.Function;


@Data
@SuppressWarnings("rawtypes")
public class RelationCache {

    // 关联表的entity类
    private Class<? extends Model<?>> foreignEntityClass;

    // 主表关联属性的getter
    private Function<Object, Object> localPropertyGetter;

    // 关联表的关联属性的getter
    private SFunction foreignPropertyGetter;

    // 本表设置关联属性的setter
    private BiConsumer relationEntitySetter;


}
