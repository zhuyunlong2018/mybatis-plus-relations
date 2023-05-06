package com.zyl.mybatisplus.relations.resolver;

import com.zyl.mybatisplus.relations.annotations.ManyBindMany;
import com.zyl.mybatisplus.relations.exceptions.RelationAnnotationException;

import java.lang.reflect.Field;

public class ManyBindManyResolver extends Resolver<ManyBindMany>{

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
    protected void checkFieldType(Field field) {
        if (field.getType() != java.util.List.class) {
            throw new RelationAnnotationException(field.getName() + "绑定对象需要为List类型");
        }
    }

    @Override
    protected void setProperty() {
        localProperty = relationAnnotation.localProperty();
        foreignProperty = relationAnnotation.foreignProperty();
        linkLocalProperty = relationAnnotation.linkLocalProperty();
        linkForeignProperty = relationAnnotation.linkForeignProperty();
        cache.setApplySql(relationAnnotation.applySql());
        cache.setLastSql(relationAnnotation.lastSql());
        cache.setLinkEntityClass(relationAnnotation.linkModel());
        cache.setLinkApplySql(relationAnnotation.linkApplySql());
        cache.setLinkLastSql(relationAnnotation.linkLastSql());
    }
}
