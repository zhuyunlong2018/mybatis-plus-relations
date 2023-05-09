package com.zyl.mybatisplus.relations.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zyl.mybatisplus.relations.binder.IBinder;

import java.util.function.Consumer;

/**
 * 关联处理器，用于一对一、一对多、多对多的不同场景关联查询
 *
 * @param <T>
 * @param <R>
 */
public interface IHandler<T, R> {

    /**
     * 获取处理器是否处理完毕，返回true后，处理器再调用其他方法都将失效
     */
    Boolean getEnded();

    /**
     * 关联副表查询构造器，使用方式如下，其中Dept为关联副表模型
     * .query(wrapper -> wrapper.eq(Dept::getId, 1))
     */
    IHandler<T, R> query(Consumer<LambdaQueryWrapper<R>> lambdaWrapperFunc);

    /**
     * 深度绑定将提前触发end，所以需要放在最后，
     * 调用将获得一个新的binder，此binder主表为原binder的副表
     * 使用示例如下
     * .deepWith(binder -> {
     * binder.bindOne(UserSkillVO::getSkill).end();
     * })
     */
    IBinder<T> deepWith(Consumer<IBinder<R>> binderConsumer);

    /**
     * 返回绑定器，调用此法方法一般为本次handler处理完毕，并且需要返回binder去构建新的关联handler
     */
    IBinder<T> binder();

    /**
     * 结束，一般为本次handler结束，并且不继续进行其他字段的关联操作，直接结束
     */
    void end();
}
