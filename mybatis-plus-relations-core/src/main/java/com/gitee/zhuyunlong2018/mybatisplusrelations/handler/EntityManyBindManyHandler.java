package com.gitee.zhuyunlong2018.mybatisplusrelations.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.gitee.zhuyunlong2018.mybatisplusrelations.cache.RelationCache;
import com.gitee.zhuyunlong2018.mybatisplusrelations.binder.Binder;
import com.gitee.zhuyunlong2018.mybatisplusrelations.utils.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class EntityManyBindManyHandler<T, R> extends EntityBindManyHandler<T, R> implements IManyBindHandler<T, R> {

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

    /**
     * 中间表查询构造，多对多时才有用，使用方式如下，其中UserSkillRelation为中间表模型
     * .linkQuery((LambdaQueryWrapper<UserSkillRelation> wrapper) -> {
     * wrapper.gt(UserSkillRelation::getScore, 90);
     * })
     *
     * @param lambdaWrapperFunc
     * @param <L>
     * @return
     */
    @SuppressWarnings({"unchecked"})
    public <L extends Model<?>> IManyBindHandler<T, R> linkQuery(Consumer<LambdaQueryWrapper<L>> lambdaWrapperFunc) {
        this.linkLambdaWrapperFunc = (Consumer<LambdaQueryWrapper<?>>) (Object) lambdaWrapperFunc;
        return this;
    }
}
