package com.zyl.mybatisplus.relations.utils;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.io.Serializable;
import java.lang.invoke.*;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class CreateFunctionUtil {

    private static final MethodHandles.Lookup lookup = MethodHandles.lookup();

    @SuppressWarnings("unchecked")
    public static <T> SFunction<T, ?> createSFunction(Class<T> clazz, String methodName) {
        try {
            final Method method = clazz.getMethod(methodName);
            final MethodHandle getMethodHandle = lookup.unreflect(method);
            //动态调用点
            final CallSite getCallSite = LambdaMetafactory.altMetafactory(
                    lookup
                    , "apply"
                    , MethodType.methodType(SFunction.class)
                    , MethodType.methodType(Object.class, Object.class)
                    , getMethodHandle
                    , MethodType.methodType(Object.class, clazz)
                    , LambdaMetafactory.FLAG_SERIALIZABLE
                    , Serializable.class
            );
            return (SFunction<T, ?>) getCallSite.getTarget().invokeExact();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        System.out.println("SFunction 创建失败! {},{}" + clazz + "." + methodName);
        return null;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T> Function<T, ?> createGetFunction(Class<T> clazz, String methodName) {
        try {
            //获取方法句柄
            final Method getMethod = clazz.getMethod(methodName);
            final MethodHandle getMethodHandle = lookup.unreflect(getMethod);
            //动态调用点
            final CallSite getCallSite = LambdaMetafactory.metafactory(
                    lookup
                    , "apply"
                    , MethodType.methodType(Function.class)
                    , MethodType.methodType(Object.class, Object.class)
                    , getMethodHandle
                    , MethodType.methodType(List.class, clazz)
            );
            return (Function) getCallSite.getTarget().invokeExact();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        System.out.println("Function 创建失败! {},{}" + clazz + "." + methodName);
        return null;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T> BiConsumer<T, ?> createSetFunction(Class<T> clazz, String methodName, Class<?> methodParamClazz) {
        try {
            final Method method = clazz.getMethod(methodName, methodParamClazz);
            final MethodHandle methodHandle = lookup.unreflect(method);
            final CallSite callSite = LambdaMetafactory.metafactory(
                    lookup
                    , "accept"
                    , MethodType.methodType(BiConsumer.class)
                    , MethodType.methodType(void.class, Object.class, Object.class)
                    , methodHandle
                    , MethodType.methodType(void.class, clazz, methodParamClazz)
            );
            return (BiConsumer) callSite.getTarget().invokeExact();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        System.out.println("BiConsumer 创建失败! {},{}" + clazz + "." + methodName);
        return null;
    }
}
