package com.zyl.mybatisplus.relations.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.zyl.mybatisplus.relations.RelationCache;
import com.zyl.mybatisplus.relations.binder.Binder;
import com.zyl.mybatisplus.relations.utils.StringUtils;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;

/**
 * list绑定多对多的关系
 *
 * @param <T>
 * @param <R>
 */
public class ListManyBindManyHandler<T, R> extends ListBindManyHandler<T, R> implements IManyBindHandler<T, R> {

    /**
     * 连接表的外键集合
     */
    private Set<?> linkForeignProperties;

    public ListManyBindManyHandler(List<T> list, Binder<T> binder, RelationCache cache) {
        super(list, binder, cache);
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
        Set<?> localProperties = list.stream()
                .map(s -> cache.getLocalPropertyGetter().apply(s)).collect(toSet());
        // 用批量Id查询用户信息
        LambdaQueryWrapper<R> linkWrapper = Wrappers.lambdaQuery(cache.getLinkEntityClass())
                .in(cache.getLinkLocalPropertyGetter(), localProperties);
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
            // 中间表hashMap
            HashMap linkMap = (HashMap) linkList.stream()
                    .collect(groupingBy(cache.getLinkLocalPropertyGetter()));

            // 查询关联数据
            foreignEntityList = getForeignModel()
                    .selectList((LambdaQueryWrapper) getQueryWrapper());
            foreignEntityList = covertListModelToVO(foreignEntityList);
            // 关联表hashMap
            HashMap<Object, List<Object>> linkDataMap = (HashMap) foreignEntityList
                    .stream()
                    .collect(groupingBy(cache.getForeignPropertyGetter()));
            // 组装
            list.forEach(e -> {
                List<Object> linkData = (List) linkMap.get(cache.getLocalPropertyGetter().apply(e));
                if (null == linkData || linkData.isEmpty()) {
                    return;
                }
                List<List<Object>> subList = (List) linkData.stream()
                        .map(cache.getLinkForeignPropertyGetter())
                        .map(linkDataMap::get)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());
                List<Object> foreignList = subList.stream()
                        .flatMap(List::stream)
                        .collect(Collectors.toList());
                cache.getRelationEntitySetter().accept(e, foreignList);
                if (!StringUtils.isEmpty(cache.getIterateLinkMethod())) {
                    // 进入模型迭代器
                    List<Object> iterateData = linkData.stream()
                            .filter(
                                    linkItem ->
                                            linkDataMap.containsKey(cache.getLinkForeignPropertyGetter().apply(linkItem))
                            )
                            .collect(Collectors.toList());
                    cache.getIterateLinkMethodSetter().accept(e, iterateData);
                }
            });
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
