package com.gitee.zhuyunlong2018.mybatisplusrelations.func;

import java.io.Serializable;

/**
 * getter方法接口定义
 */
@FunctionalInterface
public interface IGetter<T, R> extends Serializable {
    R apply(T source);
}
