package com.zyl.mybatisplus.relations.resolver;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.zyl.mybatisplus.relations.RelationCache;
import com.zyl.mybatisplus.relations.exceptions.RelationAnnotationException;
import com.zyl.mybatisplus.relations.utils.CreateFunctionUtil;
import com.zyl.mybatisplus.relations.utils.BeanUtils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class Resolver<T> {

    /**
     * 注解实例，hasOne,hasOne ...
     */
    protected final T relationAnnotation;

    /**
     * 关联Field
     */
    protected Field field;

    /**
     * 主表实体类
     */
    protected final Class<?> localEntityClass;

    /**
     * 关联表实体类
     */
    protected Class<?> foreignEntityClass;

    /**
     * 主表关联字段属性
     */
    protected String localProperty;

    /**
     * 被关联表字段属性
     */
    protected String foreignProperty;

    /**
     * 关联注解缓存类
     */
    protected RelationCache cache = new RelationCache();
    ;

    public Resolver(T relationAnnotation, Class<?> localEntityClass) {
        this.relationAnnotation = relationAnnotation;
        this.localEntityClass = localEntityClass;
        setProperty();
    }

    public void resolve(Field field) {
        this.field = field;
        checkFieldType();
        field.setAccessible(true);
        setForeignEntityClass();
        setLocalPropertyGetter();
        setForeignPropertyGetter();
        setRelationPropertySetter();
        cache.setFieldName(field.getName());
        RelationCache.putCache(localEntityClass, field.getName(), cache);
    }

    abstract protected void checkFieldType();

    protected void setForeignEntityClass() {
        Type genericType = field.getGenericType();
        // 如果是泛型参数的类型
        if (genericType instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) genericType;
            //得到泛型里的class类型对象
            foreignEntityClass = (Class<?>) pt.getActualTypeArguments()[0];
            cache.setForeignEntityClass((Class<? extends Model<?>>) foreignEntityClass);
        }
        if (!Model.class.isAssignableFrom(foreignEntityClass)) {
            throw new RelationAnnotationException(foreignEntityClass.getName() + "需要继承Modal类");
        }
    }

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

    protected void setRelationPropertySetter() {
        final BiConsumer<?, ?> setterFunc = CreateFunctionUtil.createSetFunction(localEntityClass,
                BeanUtils.getSetterMethodName(field.getName()), List.class);
        cache.setRelationEntitySetter(setterFunc);
    }
}
