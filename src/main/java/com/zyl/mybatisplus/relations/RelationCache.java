package com.zyl.mybatisplus.relations;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.util.function.BiConsumer;
import java.util.function.Function;


@Data
@SuppressWarnings("rawtypes")
public class RelationCache {

    // 关联表的entity类
    private Class<? extends Model<?>> foreignEntityClass;

    // 主表关联属性的getter
    private Function<Object, Object> localPropertyGetter;

    // 关联表的关联属性的getter
    private SFunction foreignPropertyGetter;

    // 本表设置关联属性的setter
    private BiConsumer relationEntitySetter;

    // 中间拼接的sql
    private String applySql;

    // 最后拼接的sql
    private String lastSql;


    /**
     * ===========================
     * 以下属性用于多对多（ManyBindMany)的查询
     * ===========================
     */
    // 中间链接表的entity类
    private Class<? extends Model<?>> linkEntityClass;

    // 中间表中主表关联属性的getter
    private SFunction linkLocalPropertyGetter;

    // 总监表中的关联属性的getter
    private SFunction linkForeignPropertyGetter;

    // 中间表的中间拼接的sql
    private String linkApplySql;

    // 中间表的最后拼接的sql
    private String linkLastSql;

    @Override
    public String toString() {
        return "RelationCache{" +
                "foreignEntityClass=" + foreignEntityClass +
                ", localPropertyGetter=" + localPropertyGetter +
                ", foreignPropertyGetter=" + foreignPropertyGetter +
                ", relationEntitySetter=" + relationEntitySetter +
                ", applySql='" + applySql + '\'' +
                ", lastSql='" + lastSql + '\'' +
                '}';
    }
}
