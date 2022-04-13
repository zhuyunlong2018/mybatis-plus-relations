package com.zyl.mybatisplus.relations.resolver;

import com.zyl.mybatisplus.relations.RelationCache;
import com.zyl.mybatisplus.relations.Relations;
import com.zyl.mybatisplus.relations.annotations.BindOne;
import com.zyl.mybatisplus.relations.exceptions.RelationAnnotationException;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.lang.reflect.Field;

public class BindOneResolver extends Resolver<BindOne>{

    public BindOneResolver(BindOne relationAnnotation, Class<?> localEntityClass) {
        super(relationAnnotation, localEntityClass);
    }

    @Override
    public void resolve(Field field) {
        if (!Model.class.isAssignableFrom(field.getType())) {
            throw new RelationAnnotationException(field.getName() + "绑定对象需要继承Model类型");
        }
        field.setAccessible(true);
        setForeignEntityClass(field);
        setLocalPropertyGetter();
        setForeignPropertyGetter();
        setRelationPropertySetter(field, foreignEntityClass);
        Relations.relationMap.put(Relations.cacheKey(localEntityClass, field.getName()),
                cache);
    }

    @Override
    protected void setForeignEntityClass(Field field) {
        cache = new RelationCache();
        foreignEntityClass = field.getType();
        cache.setForeignEntityClass((Class<? extends Model<?>>) foreignEntityClass);
    }

    @Override
    protected void setProperty() {
        localProperty = relationAnnotation.localProperty();
        foreignProperty = relationAnnotation.foreignProperty();
    }
}
