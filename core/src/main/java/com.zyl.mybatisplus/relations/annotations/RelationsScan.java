package com.zyl.mybatisplus.relations.annotations;

import com.zyl.mybatisplus.relations.MyAnnotationBeanRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Import(MyAnnotationBeanRegistrar.class)
public @interface RelationsScan {

    String[] value() default {};

    String[] basePackages() default {};
}
