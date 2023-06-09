package com.gitee.zhuyunlong2018.mybatisplusrelations.binder;

import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.gitee.zhuyunlong2018.mybatisplusrelations.func.IGetter;
import com.gitee.zhuyunlong2018.mybatisplusrelations.handler.IHandler;
import com.gitee.zhuyunlong2018.mybatisplusrelations.handler.IManyBindHandler;

import java.util.List;

/**
 * 绑定器，绑定主表关联模型和绑定副表关联处理器
 *
 * @param <T>
 */
public interface IBinder<T> {

    /**
     * 绑定多对多
     */
    <R extends Model<?>> IManyBindHandler<T, R> manyBindMany(IGetter<T, List<R>> propertyGetter);

    /**
     * 绑定一对多
     */
    <R extends Model<?>> IHandler<T, R> bindMany(IGetter<T, List<R>> propertyGetter);

    /**
     * 绑定一对一
     */
    <R extends Model<?>> IHandler<T, R> bindOne(IGetter<T, R> propertyGetter);

    /**
     * 最终进行汇总进行执行sql语句
     */
    void end();
}
