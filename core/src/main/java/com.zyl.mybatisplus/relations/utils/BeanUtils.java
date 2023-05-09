package com.zyl.mybatisplus.relations.utils;


import com.zyl.mybatisplus.relations.exceptions.RelationAnnotationException;
import com.zyl.mybatisplus.relations.func.IGetter;
import com.zyl.mybatisplus.relations.func.ISetter;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeanUtils extends org.springframework.beans.BeanUtils {

    /**
     * 缓存类-Lambda的映射关系
     */
    private static Map<Class, SerializedLambda> CLASS_LAMBDA_CACHE = new ConcurrentHashMap<>();

    private static String substringAfter(String target, String prefix) {
        return target.substring(prefix.length());
    }

    private static String uncapFirst(String target) {
        return target.substring(0, 1).toLowerCase() + target.substring(1);
    }

    /***
     * 转换方法引用为属性名
     * @param fn
     * @return
     */
    public static <T> String convertToFieldName(IGetter<T, ?> fn) {
        SerializedLambda lambda = getSerializedLambda(fn);
        String methodName = lambda.getImplMethodName();
        String prefix = null;
        if (methodName.startsWith("get")) {
            prefix = "get";
        } else if (methodName.startsWith("is")) {
            prefix = "is";
        }
        if (prefix == null) {
            throw new RelationAnnotationException("无效的getter方法: " + methodName);
        }
        // 截取get/is之后的字符串并转换首字母为小写（S为diboot项目的字符串工具类，可自行实现）
        return uncapFirst(substringAfter(methodName, prefix));
    }

    /***
     * 转换setter方法引用为属性名
     * @param fn
     * @return
     */
    public static <T, R> String convertToFieldName(ISetter<T, R> fn) {
        SerializedLambda lambda = getSerializedLambda(fn);
        String methodName = lambda.getImplMethodName();
        if (!methodName.startsWith("set")) {
            throw new RelationAnnotationException("无效的setter方法: " + methodName);
        }
        // 截取set之后的字符串并转换首字母为小写（S为diboot项目的字符串工具类，可自行实现）
        return uncapFirst(substringAfter(methodName, "set"));
    }

    /***
     * 获取类对应的Lambda
     * @param fn
     * @return
     */
    private static SerializedLambda getSerializedLambda(Serializable fn) {
        //先检查缓存中是否已存在
        SerializedLambda lambda = CLASS_LAMBDA_CACHE.get(fn.getClass());
        if (lambda == null) {
            try {//提取SerializedLambda并缓存
                Method method = fn.getClass().getDeclaredMethod("writeReplace");
                method.setAccessible(Boolean.TRUE);
                lambda = (SerializedLambda) method.invoke(fn);
                CLASS_LAMBDA_CACHE.put(fn.getClass(), lambda);
            } catch (Exception e) {
                throw new RelationAnnotationException("获取SerializedLambda异常, class=" + fn.getClass().getSimpleName());
            }
        }
        return lambda;
    }

    /**
     * 根据属性名称和java类型，获取对应的getter方法名
     */
    public static String getGetterMethodName(String property, boolean isBoolean) {
        StringBuilder sb = new StringBuilder();
        sb.append(property);
        if (Character.isLowerCase(sb.charAt(0))) {
            if (sb.length() == 1 || !Character.isUpperCase(sb.charAt(1))) {
                sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            }
        }
        if (isBoolean) {
            sb.insert(0, "is");
        } else {
            sb.insert(0, "get");
        }
        return sb.toString();
    }

    /**
     * 根据属性名称获取对应的setter方法名称
     *
     * @param property
     * @return
     */
    public static String getSetterMethodName(String property) {
        StringBuilder sb = new StringBuilder();
        sb.append(property);
        if (Character.isLowerCase(sb.charAt(0))) {
            if (sb.length() == 1 || !Character.isUpperCase(sb.charAt(1))) {
                sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
            }
        }
        sb.insert(0, "set");
        return sb.toString();
    }

    /**
     * Bean属性复制工具方法。
     *
     * @param dest 目标对象
     * @param src  源对象
     */
    public static void copyBeanProp(Object dest, Object src) {
        try {
            copyProperties(src, dest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}