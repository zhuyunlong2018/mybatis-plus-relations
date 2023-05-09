package com.zyl.mybatisplus.relations.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface BindMany {

    /**
     * 本表关联属性,填写entity的属性，不是数据库字段
     */
    String localProperty();

    /**
     * 子表关联属性,填写entity的属性，不是数据库字段
     */
    String foreignProperty();

    /**
     * 添加的sql，调用LambdaQueryWrapper的apply方法
     */
    String applySql() default "";

    /**
     * 最后拼接的sql语句，调用LambdaQueryWrapper的last方法
     */
    String lastSql() default "";
}
