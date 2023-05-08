package com.zyl.mybatisplus.relations.resolver;

import com.zyl.mybatisplus.relations.Relations;
import com.zyl.mybatisplus.relations.annotations.BindOne;
import com.zyl.mybatisplus.relations.exceptions.RelationAnnotationException;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.zyl.mybatisplus.relations.utils.BeanUtils;
import com.zyl.mybatisplus.relations.utils.CreateFunctionUtil;

import java.lang.reflect.Field;
import java.util.function.BiConsumer;

public class BindOneResolver extends Resolver<BindOne>{

    public BindOneResolver(BindOne relationAnnotation, Class<?> localEntityClass) {
        super(relationAnnotation, localEntityClass);
    }

    @Override
    protected void checkFieldType() {
        if (!Model.class.isAssignableFrom(field.getType())) {
            throw new RelationAnnotationException(field.getName() + "绑定对象需要继承Model类型");
        }
    }

    @Override
    protected void setForeignEntityClass() {
        foreignEntityClass = field.getType();
        cache.setForeignEntityClass((Class<? extends Model<?>>) foreignEntityClass);
    }

    @Override
    protected void setRelationPropertySetter() {
        final BiConsumer<?, ?> setterFunc = CreateFunctionUtil.createSetFunction(localEntityClass,
                BeanUtils.getSetterMethodName(field.getName()), foreignEntityClass);
        cache.setRelationEntitySetter(setterFunc);
    }

    @Override
    protected void setProperty() {
        localProperty = relationAnnotation.localProperty();
        foreignProperty = relationAnnotation.foreignProperty();
        cache.setApplySql(relationAnnotation.applySql());
        cache.setLastSql(relationAnnotation.lastSql());
    }

}
