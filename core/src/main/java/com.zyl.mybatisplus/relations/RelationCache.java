package com.zyl.mybatisplus.relations;

import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.zyl.mybatisplus.relations.exceptions.RelationAnnotationException;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;


@SuppressWarnings("rawtypes")
public class RelationCache {

    /**
     * 存储关联模型的绑定信息
     */
    private static Map<String, RelationCache> relationMap = new HashMap<>();

    /**
     * 获取缓存key
     *
     * @param clazz 模型类
     * @param field 关联字段属性
     * @return
     */
    private static String cacheKey(Class<?> clazz, String field) {
        return clazz.getName() + "." + field;
    }

    /**
     * 添加模型关联缓存
     *
     * @param entityClass
     * @param property
     * @param cache
     */
    public static void putCache(Class<?> entityClass, String property, RelationCache cache) {
        relationMap.put(cacheKey(entityClass, property), cache);
    }

    /**
     * 获取模型关联缓存
     *
     * @param entityClass
     * @param property
     * @return
     */
    public static RelationCache getCache(Class<?> entityClass, String property) {
        if (!relationMap.containsKey(cacheKey(entityClass, property))) {
            System.out.println(relationMap);
            System.out.println(entityClass);
            System.out.println(property);
            throw new RelationAnnotationException("模型属性未绑定关系，获取关系缓存错误" + entityClass + property);
        }
        return relationMap.get(cacheKey(entityClass, property));
    }

    // 关联表的entity类
    private Class<? extends Model<?>> foreignEntityClass;

    /**
     * 用于标识模型关系缓存的属性名，即加入@HasOne等注解的属性名，由localPropertyGetter生成
     */
    private String fieldName;

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

    // 中间表迭代器方法
    private BiConsumer iterateLinkMethodSetter;

    // 中间表的中间拼接的sql
    private String linkApplySql;

    // 中间表的最后拼接的sql
    private String linkLastSql;

    // 中间表迭代
    private String iterateLinkMethod;

    public Class<? extends Model<?>> getForeignEntityClass() {
        return foreignEntityClass;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Function<Object, Object> getLocalPropertyGetter() {
        return localPropertyGetter;
    }

    public SFunction getForeignPropertyGetter() {
        return foreignPropertyGetter;
    }

    public BiConsumer getRelationEntitySetter() {
        return relationEntitySetter;
    }

    public String getApplySql() {
        return applySql;
    }

    public String getLastSql() {
        return lastSql;
    }

    public Class<? extends Model<?>> getLinkEntityClass() {
        return linkEntityClass;
    }

    public SFunction getLinkLocalPropertyGetter() {
        return linkLocalPropertyGetter;
    }

    public SFunction getLinkForeignPropertyGetter() {
        return linkForeignPropertyGetter;
    }

    public BiConsumer getIterateLinkMethodSetter() {
        return iterateLinkMethodSetter;
    }

    public String getLinkApplySql() {
        return linkApplySql;
    }

    public String getLinkLastSql() {
        return linkLastSql;
    }

    public String getIterateLinkMethod() {
        return iterateLinkMethod;
    }

    public void setForeignEntityClass(Class<? extends Model<?>> foreignEntityClass) {
        this.foreignEntityClass = foreignEntityClass;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setLocalPropertyGetter(Function<Object, Object> localPropertyGetter) {
        this.localPropertyGetter = localPropertyGetter;
    }

    public void setForeignPropertyGetter(SFunction foreignPropertyGetter) {
        this.foreignPropertyGetter = foreignPropertyGetter;
    }

    public void setRelationEntitySetter(BiConsumer relationEntitySetter) {
        this.relationEntitySetter = relationEntitySetter;
    }

    public void setApplySql(String applySql) {
        this.applySql = applySql;
    }

    public void setLastSql(String lastSql) {
        this.lastSql = lastSql;
    }

    public void setLinkEntityClass(Class<? extends Model<?>> linkEntityClass) {
        this.linkEntityClass = linkEntityClass;
    }

    public void setLinkLocalPropertyGetter(SFunction linkLocalPropertyGetter) {
        this.linkLocalPropertyGetter = linkLocalPropertyGetter;
    }

    public void setLinkForeignPropertyGetter(SFunction linkForeignPropertyGetter) {
        this.linkForeignPropertyGetter = linkForeignPropertyGetter;
    }

    public void setIterateLinkMethodSetter(BiConsumer iterateLinkMethodSetter) {
        this.iterateLinkMethodSetter = iterateLinkMethodSetter;
    }

    public void setLinkApplySql(String linkApplySql) {
        this.linkApplySql = linkApplySql;
    }

    public void setLinkLastSql(String linkLastSql) {
        this.linkLastSql = linkLastSql;
    }

    public void setIterateLinkMethod(String iterateLinkMethod) {
        this.iterateLinkMethod = iterateLinkMethod;
    }

    @Override
    public String toString() {
        return "RelationCache{" +
                "foreignEntityClass=" + foreignEntityClass +
                ", fieldName='" + fieldName + '\'' +
                ", localPropertyGetter=" + localPropertyGetter +
                ", foreignPropertyGetter=" + foreignPropertyGetter +
                ", relationEntitySetter=" + relationEntitySetter +
                ", applySql='" + applySql + '\'' +
                ", lastSql='" + lastSql + '\'' +
                ", linkEntityClass=" + linkEntityClass +
                ", linkLocalPropertyGetter=" + linkLocalPropertyGetter +
                ", linkForeignPropertyGetter=" + linkForeignPropertyGetter +
                ", iterateLinkMethodSetter=" + iterateLinkMethodSetter +
                ", linkApplySql='" + linkApplySql + '\'' +
                ", linkLastSql='" + linkLastSql + '\'' +
                ", iterateLinkMethod='" + iterateLinkMethod + '\'' +
                '}';
    }
}
