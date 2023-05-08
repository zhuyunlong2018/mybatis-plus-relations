package com.zyl.mybatisplus.relations.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zyl.mybatisplus.relations.RelationCache;
import com.zyl.mybatisplus.relations.binder.Binder;
import com.zyl.mybatisplus.relations.utils.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class EntityManyBindManyHandler<T, R> extends EntityBindManyHandler<T, R> {

    /**
     * 连接表的外键集合
     */
    private Set<?> linkForeignProperties;

    public EntityManyBindManyHandler(T entity, Binder<T> binder, RelationCache cache) {
        super(entity, binder, cache);
    }

    @SuppressWarnings({"unchecked"})
    @Override
    protected LambdaQueryWrapper<R> getQueryWrapper() {
        LambdaQueryWrapper<R> wrapper = (LambdaQueryWrapper<R>) Wrappers.lambdaQuery(cache.getForeignEntityClass())
                .in(cache.getForeignPropertyGetter(), linkForeignProperties);
        wrapper.apply(!StringUtils.isEmpty(cache.getApplySql()), cache.getApplySql());
        if (lambdaWrapperFunc != null) {
            lambdaWrapperFunc.accept(wrapper);
        }
        wrapper.last(!StringUtils.isEmpty(cache.getLastSql()), cache.getLastSql());
        return wrapper;
    }

    @SuppressWarnings({"unchecked"})
    private LambdaQueryWrapper<R> getLinkQueryWrapper() {
        LambdaQueryWrapper<R> linkWrapper = Wrappers.lambdaQuery(cache.getLinkEntityClass())
                .eq(cache.getLinkLocalPropertyGetter(), cache.getLocalPropertyGetter().apply(entity));
        linkWrapper.apply(!StringUtils.isEmpty(cache.getLinkApplySql()), cache.getLinkApplySql());
        if (linkLambdaWrapperFunc != null) {
            linkLambdaWrapperFunc.accept(linkWrapper);
        }
        linkWrapper.last(!StringUtils.isEmpty(cache.getLinkLastSql()), cache.getLinkLastSql());
        return linkWrapper;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    protected void queryRelation() {
        List linkList = getLinkModel().selectList((LambdaQueryWrapper) getLinkQueryWrapper());
        // 外键集合ID
        linkForeignProperties = (Set<?>) linkList
                .stream()
                .map(cache.getLinkForeignPropertyGetter())
                .collect(Collectors.toSet());
        if (linkForeignProperties.size() > 0) {
            foreignEntityList = (List<R>) getForeignModel()
                    .selectList((LambdaQueryWrapper) getQueryWrapper());
            // 换行VO
            foreignEntityList = covertListModelToVO(foreignEntityList);
            cache.getRelationEntitySetter().accept(entity, foreignEntityList);
            if (!StringUtils.isEmpty(cache.getIterateLinkMethod())) {
                // 进入模型迭代器
                cache.getIterateLinkMethodSetter().accept(entity, linkList);
            }
        }
    }
}
