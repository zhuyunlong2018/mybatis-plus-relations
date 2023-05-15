package com.gitee.zhuyunlong2018.mybatisplusrelations.annotations;

import com.gitee.zhuyunlong2018.mybatisplusrelations.scanner.MyAnnotationBeanRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Import(MyAnnotationBeanRegistrar.class)
public @interface RelationScan {

    String[] value() default {};

    String[] basePackages() default {};
}
