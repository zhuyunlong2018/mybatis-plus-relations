package com.zyl.mybatisplus.relations.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.util.function.Consumer;

/**
 * 多对多的关联处理器
 *
 * @param <T>
 * @param <R>
 */
public interface IManyBindHandler<T, R> extends IHandler<T, R> {

    /**
     * 中间表查询构造，多对多时才有用，使用方式如下，其中UserSkillRelation为中间表模型
     * 此处必须显示指定lambda的参数wrapper的类型，用于表明泛型L的类型
     * .linkQuery((LambdaQueryWrapper<UserSkillRelation> wrapper) -> {
     *      wrapper.gt(UserSkillRelation::getScore, 90);
     * })
     */
    <L extends Model<?>> IManyBindHandler<T, R> linkQuery(Consumer<LambdaQueryWrapper<L>> lambdaWrapperFunc);
}
