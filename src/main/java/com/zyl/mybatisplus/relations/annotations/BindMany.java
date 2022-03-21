package com.zyl.mybatisplus.relations.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface BindMany {

//    // 本表关联属性
    String localProperty();

    // 子表关联属性
    String foreignProperty();
}
