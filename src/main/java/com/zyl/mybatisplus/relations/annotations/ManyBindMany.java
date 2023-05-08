package com.zyl.mybatisplus.relations.annotations;

import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({METHOD, FIELD})
@Retention(RUNTIME)
public @interface ManyBindMany {

    /**
     * 本表关联属性,填写entity的属性，不是数据库字段
     */
    String localProperty();

    /**
     * 子表关联属性,填写entity的属性，不是数据库字段
     */
    String foreignProperty();

    /**
     * 中间表模型
     * @return
     */
    Class<? extends Model<?>> linkModel();

    /**
     * 连接表中本表关联属性,填写entity的属性，不是数据库字段，默认和localProperty一致
     */
    String linkLocalProperty() default "";

    /**
     * 连接表中子表关联属性,填写entity的属性，不是数据库字段，默认和foreignProperty一致
     */
    String linkForeignProperty() default "";

    /**
     * 中间表添加的sql，调用LambdaQueryWrapper的apply方法
     */
    String linkApplySql() default "";

    /**
     * 中间表最后拼接的sql语句，调用LambdaQueryWrapper的last方法
     */
    String linkLastSql() default "";

    /**
     * 添加的sql，调用LambdaQueryWrapper的apply方法
     */
    String applySql() default "";

    /**
     * 最后拼接的sql语句，调用LambdaQueryWrapper的last方法
     */
    String lastSql() default "";

    /**
     * 中间表List迭代器，可以在主模型中设置一个方法来接收中间表的迭代，此处填写模型的方法名称
     * 例如setRelationTableProperty
     * 方法体：public void setRelationTableProperty(List<linkModel> linkModels)
     * @return
     */
    String iterateLinkMethod() default "";

}
