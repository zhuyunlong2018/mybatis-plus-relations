package com.zyl.mybatisplus.relations.resolver;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.zyl.mybatisplus.relations.RelationCache;
import com.zyl.mybatisplus.relations.Relations;
import com.zyl.mybatisplus.relations.annotations.BindMany;
import com.zyl.mybatisplus.relations.exceptions.RelationAnnotationException;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class BindManyResolver extends Resolver<BindMany> {

    public BindManyResolver(BindMany relationAnnotation, Class<?> localEntityClass) {
        super(relationAnnotation, localEntityClass);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public void resolve(Field field) {
        if (field.getType() != java.util.List.class) {
            throw new RelationAnnotationException(field.getName() + "绑定对象需要为List类型");
        }
        field.setAccessible(true);
        // 如果是List类型，得到其Generic的类型
        setForeignEntityClass(field);
        setLocalPropertyGetter();
        setForeignPropertyGetter();
        setRelationPropertySetter(field, List.class);
        Relations.relationMap.put(Relations.cacheKey(localEntityClass, field.getName()),
                cache);
    }

    @Override
    protected void setForeignEntityClass(Field field) {
        Type genericType = field.getGenericType();
        cache = new RelationCache();
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

    @Override
    protected void setProperty() {
        localProperty = relationAnnotation.localProperty();
        foreignProperty = relationAnnotation.foreignProperty();
    }

}
