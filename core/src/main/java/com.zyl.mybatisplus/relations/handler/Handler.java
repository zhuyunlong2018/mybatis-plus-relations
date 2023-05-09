package com.zyl.mybatisplus.relations.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.zyl.mybatisplus.relations.RelationCache;
import com.zyl.mybatisplus.relations.binder.Binder;
import com.zyl.mybatisplus.relations.binder.IBinder;
import com.zyl.mybatisplus.relations.exceptions.RelationAnnotationException;
import com.zyl.mybatisplus.relations.utils.BeanUtils;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * 关联处理器
 * @param <T>
 * @param <R>
 */
public abstract class Handler<T, R> implements IHandler<T, R> {
    /**
     * 关系构造器
     */
    protected Binder<T> binder;
    /**
     * 补充查询器
     */
    protected Consumer<LambdaQueryWrapper<R>> lambdaWrapperFunc;

    /**
     * 中间表补充查询器，多对多的时候使用
     */
    protected Consumer<LambdaQueryWrapper<?>> linkLambdaWrapperFunc;

    /**
     * 关联注解关系缓存
     */
    protected RelationCache cache;

    /**
     * 本次处理是否结束
     */
    protected Boolean ended = false;

    /**
     * 初始化关联表query wrapper
     *
     * @return
     */
    @SuppressWarnings({"rawtypes"})
    protected abstract LambdaQueryWrapper<R> getQueryWrapper();

    /**
     * 数据库查询被关联表数据
     */
    @SuppressWarnings({"rawtypes"})
    protected abstract void queryRelation();


    public Boolean getEnded() {
        return ended;
    }

    /**
     * 关联副表查询构造器，使用方式如下，其中Dept为关联副表模型
     * .query(wrapper -> wrapper.eq(Dept::getId, 1))
     *
     * @param lambdaWrapperFunc
     * @return
     */
    public Handler<T, R> query(Consumer<LambdaQueryWrapper<R>> lambdaWrapperFunc) {
        this.lambdaWrapperFunc = lambdaWrapperFunc;
        return this;
    }

    /**
     * 模型数组转换为VO数组
     * 深度绑定时使用
     *
     * @param modelList
     * @return
     */
    @SuppressWarnings({"unchecked"})
    protected List<R> covertListModelToVO(List<R> modelList) {
        if (null == modelList || modelList.isEmpty()) {
            return modelList;
        }
        if (!cache.getForeignEntityClass().equals(modelList.get(0).getClass())) {
            // 如果关联的是vo，而不是直接关联model，需要转换
            return (List<R>) modelList.stream()
                    .map(e -> {
                        try {
                            Model<?> model = cache.getForeignEntityClass().newInstance();
                            BeanUtils.copyBeanProp(model, e);
                            return model;
                        } catch (InstantiationException | IllegalAccessException instantiationException) {
                            throw new RelationAnnotationException("VO构造函数错误");
                        }
                    })
                    .collect(Collectors.toList());
        }
        return modelList;
    }

    /**
     * 模型数组转换为VO对象
     * 深度绑定时使用
     * @param model
     * @return
     */
    @SuppressWarnings({"unchecked"})
    protected R covertVoToModel(R model) {
        if (null == model) {
            return null;
        }
        if (!cache.getForeignEntityClass().equals(model.getClass())) {
            // 如果关联的是vo，而不是直接关联model，需要转换
            try {
                Model<?> entity = cache.getForeignEntityClass().newInstance();
                BeanUtils.copyBeanProp(entity, model);
                return (R) entity;
            } catch (InstantiationException | IllegalAccessException instantiationException) {
                throw new RelationAnnotationException("VO构造函数错误");
            }
        }
        return model;
    }

    /**
     * 获取被关联表的entity实例
     *
     * @return
     */
    protected Model<?> getForeignModel() {
        Model<?> model = null;
        try {
            model = cache.getForeignEntityClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        assert model != null;
        return model;
    }

    /**
     * 获取中间表model， 多对多时使用
     *
     * @return
     */
    protected Model<?> getLinkModel() {
        Model<?> linkModel = null;
        try {
            linkModel = cache.getLinkEntityClass().newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        // 中间表集合
        assert linkModel != null;
        return linkModel;
    }

    /**
     * 如果需要绑定多个关联属性，这返回binder
     *
     * @return
     */
    public IBinder<T> binder() {
        return binder;
    }

    /**
     * 结束关联查询，进行查询
     */
    public void end() {
        if (!ended) {
            ended = true;
            queryRelation();
            // 通知binder处理其他handler
            binder.end();
        }
    }
}
