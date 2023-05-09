package com.zyl.mybatisplus.relations.resolver;

import com.zyl.mybatisplus.relations.annotations.BindMany;
import com.zyl.mybatisplus.relations.exceptions.RelationAnnotationException;

public class BindManyResolver extends Resolver<BindMany> {

    public BindManyResolver(BindMany relationAnnotation, Class<?> localEntityClass) {
        super(relationAnnotation, localEntityClass);
    }

    @Override
    protected void checkFieldType() {
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
