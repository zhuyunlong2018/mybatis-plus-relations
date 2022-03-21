package com.zyl.mybatisplus.relations.resolver;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.zyl.mybatisplus.relations.RelationCache;
import com.zyl.mybatisplus.relations.utils.CreateFunctionUtil;
import com.zyl.mybatisplus.relations.utils.BeanUtils;

import java.lang.reflect.Field;
import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class Resolver<T> {

    /**
     * 注解实例，hasOne,hasOne ...
     */
    protected final T relationAnnotation;

    /**
     * 主表实体类
     */
    protected final Class<?> localEntityClass;

    /**
     * 关联表实体类
     */
    protected Class<?> foreignEntityClass;

    protected String localProperty;

    protected String foreignProperty;

    /**
     * 关联注解缓存类
     */
    protected RelationCache cache;

    public Resolver(T relationAnnotation, Class<?> localEntityClass) {
        this.relationAnnotation = relationAnnotation;
        this.localEntityClass = localEntityClass;
        setProperty();
    }

    abstract public void resolve(Field field);

    abstract protected void setForeignEntityClass(Field field);

    abstract protected void setProperty();

    @SuppressWarnings({"rawtypes", "unchecked"})
    protected void setLocalPropertyGetter() {
        final Function getterFunc = CreateFunctionUtil.createGetFunction(localEntityClass,
                BeanUtils.getGetterMethodName(localProperty, false));
        cache.setLocalPropertyGetter(getterFunc);
    }

    protected void setForeignPropertyGetter() {
        final SFunction<?, ?> getterFunc = CreateFunctionUtil.createSFunction(foreignEntityClass,
                BeanUtils.getGetterMethodName(foreignProperty, false));
        cache.setForeignPropertyGetter(getterFunc);
    }

    protected void setRelationPropertySetter(Field field, Class<?> setParamClazz) {
        final BiConsumer<?, ?> setterFunc = CreateFunctionUtil.createSetFunction(localEntityClass,
                BeanUtils.getSetterMethodName(field.getName()), setParamClazz);
        cache.setRelationEntitySetter(setterFunc);
    }
}
