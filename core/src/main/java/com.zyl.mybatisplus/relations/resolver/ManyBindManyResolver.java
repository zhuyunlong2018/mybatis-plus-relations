package com.zyl.mybatisplus.relations.resolver;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.zyl.mybatisplus.relations.annotations.ManyBindMany;
import com.zyl.mybatisplus.relations.exceptions.RelationAnnotationException;
import com.zyl.mybatisplus.relations.utils.BeanUtils;
import com.zyl.mybatisplus.relations.utils.CreateFunctionUtil;
import com.zyl.mybatisplus.relations.utils.StringUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.function.BiConsumer;

public class ManyBindManyResolver extends Resolver<ManyBindMany> {

    /**
     * 连接表实体类
     */
    protected Class<?> linkEntityClass;

    /**
     * 中间表中主表关联字段属性
     */
    protected String linkLocalProperty;

    /**
     * 中间表中被关联表字段属性
     */
    protected String linkForeignProperty;

    public ManyBindManyResolver(ManyBindMany relationAnnotation, Class<?> localEntityClass) {
        super(relationAnnotation, localEntityClass);
    }

    @Override
    protected void checkFieldType() {
        if (field.getType() != java.util.List.class) {
            throw new RelationAnnotationException(field.getName() + "绑定对象需要为List类型");
        }
    }

    @Override
    public void resolve(Field field) {
        super.resolve(field);
        setLinkForeignPropertyGetter();
        setLinkLocalPropertyGetter();
        setIterateMethod();
    }

    @Override
    protected void setProperty() {
        localProperty = relationAnnotation.localProperty();
        foreignProperty = relationAnnotation.foreignProperty();
        linkLocalProperty = relationAnnotation.linkLocalProperty();
        linkForeignProperty = relationAnnotation.linkForeignProperty();
        if (StringUtils.isEmpty(linkLocalProperty)) {
            linkLocalProperty = localProperty;
        }
        if (StringUtils.isEmpty(linkForeignProperty)) {
            linkForeignProperty = foreignProperty;
        }
        linkEntityClass = relationAnnotation.linkModel();
        cache.setApplySql(relationAnnotation.applySql());
        cache.setLastSql(relationAnnotation.lastSql());
        cache.setLinkEntityClass(relationAnnotation.linkModel());
        cache.setLinkApplySql(relationAnnotation.linkApplySql());
        cache.setLinkLastSql(relationAnnotation.linkLastSql());
        cache.setIterateLinkMethod(relationAnnotation.iterateLinkMethod());
    }

    protected void setLinkLocalPropertyGetter() {
        final SFunction<?, ?> getterFunc = CreateFunctionUtil.createSFunction(linkEntityClass,
                BeanUtils.getGetterMethodName(linkLocalProperty, false));
        cache.setLinkLocalPropertyGetter(getterFunc);
    }

    protected void setLinkForeignPropertyGetter() {
        final SFunction<?, ?> getterFunc = CreateFunctionUtil.createSFunction(linkEntityClass,
                BeanUtils.getGetterMethodName(linkForeignProperty, false));
        cache.setLinkForeignPropertyGetter(getterFunc);
    }

    /**
     * 实现迭代器
     */
    protected void setIterateMethod() {
        final BiConsumer<?, ?> setterFunc = CreateFunctionUtil.createSetFunction(localEntityClass,
                cache.getIterateLinkMethod(), List.class);
        cache.setIterateLinkMethodSetter(setterFunc);
    }
}
