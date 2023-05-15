package com.gitee.zhuyunlong2018.mybatisplusrelations.resolver;

import com.gitee.zhuyunlong2018.mybatisplusrelations.annotations.BindOne;
import com.gitee.zhuyunlong2018.mybatisplusrelations.exceptions.RelationAnnotationException;
import com.gitee.zhuyunlong2018.mybatisplusrelations.utils.BeanUtils;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.gitee.zhuyunlong2018.mybatisplusrelations.utils.CreateFunctionUtil;

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
