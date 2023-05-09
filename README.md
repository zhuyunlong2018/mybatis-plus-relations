
### mybatis-plus-relations
### mybatis-plus 关联查询

通过在entity或者vo的属性注解，可以关联查询并自动组装数据，不需要写任何xml和sql语句，生成的sql语句不使用join，而是使用索引in查询，java的stream进行组装，解决mybatis多表关联时可能存在的n+1问题

### 使用方法

#### 1. 添加扫描注解包
```java
package com.zyl.mybatisplus.config;
import com.zyl.mybatisplus.relations.ScanRelationsAnnotations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class AutoMapperConfig {
	@Bean
	public ScanRelationsAnnotations autoMapper() {
		return new ScanRelationsAnnotations(new String[] {
				"com.zyl.mybatisplus.domain", // 可能存在关系注解的包路劲
				"com.zyl.mybatisplus.entity.vo"
		});
	}

}
```

#### 2. 给关联属性添加注解
```java
package com.zyl.mybatisplus.entity.vo;

import Dept;
import User;
import com.zyl.mybatisplus.relations.annotations.BindMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ToString(callSuper = true)
public class DeptVo extends Dept {

    // 通过注解绑定关联，localProperty为当前entity的关联属性，foreignProperty为被关联进来的entity的关联属性
    @BindMany(localProperty = "deptId", foreignProperty = "deptId")
    private List<User> users;
    public DeptVo(Dept dept) {
        super(dept);
    }
}
```

目前实现@BindMany和@BindOne注解，分别表示绑定一对多和一对一，被关联模型（entity）必须是mybatis-plus的Model的子孙类

#### 3. 查询注入

```java
package com.zyl.mybatisplus.service.impl;

// import ...;


/**
 * @author explore
 * @since 2021/05/24 11:09
 **/
@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements IDeptService {

    /**
     * 查询单个部门（其中一个部门有多个用户）
     */
    @Override
    public DeptVo getOneDept(Integer deptId) {
        // 查询部门基础信息
        LambdaQueryWrapper<Dept> wrapper = Wrappers.lambdaQuery(Dept.class).eq(Dept::getDeptId, deptId);
        DeptVo deptVo = EntityUtils.toObj(getOne(wrapper), DeptVo::new);
        Relations.with(deptVo).bindMany(DeptVo::getUsers).end();
        return deptVo;
    }
    
    /**
     * 查询多个部门（其中一个部门有多个用户）
     */
    @Override
    public List<DeptVo> getDeptByList() {
        // 按条件查询部门信息
        List<DeptVo> deptVos = EntityUtils.toList(list(Wrappers.emptyWrapper()), DeptVo::new);
        Relations.with(deptVos).bindMany(DeptVo::getUsers).end(); // 绑定users属性的关系
        return deptVos;
    }
}
```

#### 4. 查询添加其他条件
```java
// Relations.with(deptVos).bindMany(DeptVo::getUsers); // 绑定关系,不进行其他查询


// 使用bindMany方法传入lambda可以获得关联表的LambdaQueryWrapper进行添加其他筛选条件
// Relations.with(deptVos).bindMany(DeptVo::getUsers).query(wrapper -> wrapper.eq(User::getUserId, 1)).end();
```




### 参考资料

- 扫描包路劲注解使用 mprelation：https://github.com/dreamyoung/mprelation.git   
- 测试用数据结构直接使用 mybatisplus-joinquery：https://gitee.com/java-spring-demo/mybatisplus-joinquery.git
- 部分代码参考：https://gitee.com/xiaokedamowang/bilibili

