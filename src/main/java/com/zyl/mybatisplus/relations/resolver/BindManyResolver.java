package com.zyl.mybatisplus.relations.resolver;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.zyl.mybatisplus.relations.RelationCache;
import com.zyl.mybatisplus.relations.Relations;
import com.zyl.mybatisplus.relations.annotations.BindMany;
import com.zyl.mybatisplus.relations.exceptions.RelationAnnotationException;
import com.zyl.mybatisplus.relations.utils.BeanUtils;
import com.zyl.mybatisplus.relations.utils.CreateFunctionUtil;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.function.BiConsumer;

public class BindManyResolver extends Resolver<BindMany> {

    public BindManyResolver(BindMany relationAnnotation, Class<?> localEntityClass) {
        super(relationAnnotation, localEntityClass);
    }

    @Override
    protected void checkFieldType(Field field) {
        if (field.getType() != java.util.List.class) {
            throw new RelationAnnotationException(field.getName() + "绑定对象需要为List类型");
        }
    }

    @Override
    protected void setProperty() {
        localProperty = relationAnnotation.localProperty();
        foreignProperty = relationAnnotation.foreignProperty();
        cache.setApplySql(relationAnnotation.applySql());
        cache.setLastSql(relationAnnotation.lastSql());
    }

}
