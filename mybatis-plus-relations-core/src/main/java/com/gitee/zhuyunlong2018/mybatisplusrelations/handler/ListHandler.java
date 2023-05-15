package com.gitee.zhuyunlong2018.mybatisplusrelations.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.gitee.zhuyunlong2018.mybatisplusrelations.cache.RelationCache;
import com.gitee.zhuyunlong2018.mybatisplusrelations.Relations;
import com.gitee.zhuyunlong2018.mybatisplusrelations.binder.Binder;
import com.gitee.zhuyunlong2018.mybatisplusrelations.binder.IBinder;
import com.gitee.zhuyunlong2018.mybatisplusrelations.utils.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import static java.util.stream.Collectors.toSet;

public abstract class ListHandler<T, R> extends Handler<T, R> {

    /**
     * 关联主表list
     */
    protected List<T> list;

    /**
     * 关联表list
     */
    protected List<R> foreignEntityList;

    public ListHandler(List<T> list, Binder<T> binder, RelationCache cache) {
        this.list = list;
        this.binder = binder;
        this.cache = cache;
    }

    @SuppressWarnings({"unchecked"})
    @Override
    protected LambdaQueryWrapper<R> getQueryWrapper() {
        // 准备deptId方便批量查询用户信息
        Set<?> localProperties = list.stream()
                .map(s -> cache.getLocalPropertyGetter().apply(s)).collect(toSet());
        // 用批量Id查询用户信息
        LambdaQueryWrapper<R> wrapper = Wrappers.lambdaQuery(cache.getForeignEntityClass())
                .in(cache.getForeignPropertyGetter(), localProperties);
        wrapper.apply(!StringUtils.isEmpty(cache.getApplySql()), cache.getApplySql());
        if (lambdaWrapperFunc != null) {
            lambdaWrapperFunc.accept(wrapper);
        }
        wrapper.last(!StringUtils.isEmpty(cache.getLastSql()), cache.getLastSql());
        return wrapper;
    }

    @Override
    public IBinder<T> deepWith(Consumer<IBinder<R>> binderConsumer) {
        end();
        binderConsumer.accept(Relations.with(foreignEntityList));
        return binder;
    }

}
