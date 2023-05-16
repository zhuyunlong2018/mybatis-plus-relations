
### mybatis-plus-relations
### mybatis-plus 关联查询

#### 简介

通过在entity或者vo的属性注解，可以关联查询并自动组装数据，不需要写任何xml和sql语句，生成的sql语句不使用join，而是使用索引in查询，java的stream进行组装，解决mybatis多表关联时可能存在的n+1问题
使用mybatis-plus的接口，可以和现有项目无侵入整合

#### 源码

[zhuyunlong2018/mybatis-plus-relations: mybatis-plus的关联模型查询插件 (github.com)](https://github.com/zhuyunlong2018/mybatis-plus-relations)

[mybatis-plus-relations: mybatis-plus的关联查询，使用注解方式操作 (gitee.com)](https://gitee.com/zhuyunlong2018/mybatis-plus-relations)

### 使用方法

添加maven依赖
```xml
<dependency>
      <groupId>io.gitee.zhuyunlong2018</groupId>
      <artifactId>mybatis-plus-relations-core</artifactId>
      <version>1.0.0</version>
</dependency>
```

#### 1. 添加扫描注解包
给SpringBoot入口程序添加@RelationScan注解即可，包名可以实现和@MapperScan一样的通配符操作，*代表一级通配符，**代表可以多级通配符，用于扫描含有关联查询注解的entity或者vo等
```java
@SpringBootApplication
@MapperScan(basePackages = "com.gitee.zhuyunlong2018.mybatisplusrelations.**.mapper")
@RelationScan({
        "com.gitee.zhuyunlong2018.mybatisplusrelations.*.vo",
        "com.gitee.zhuyunlong2018.mybatisplusrelations.entity"
})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

#### 2. 给关联属性添加注解
```java
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
#### 注解说明
##### 注解类别
- @BindOne 绑定一对一关系
- @BindMany 绑定一对多关系
- @ManyBindMany 绑定多对多关系

##### 以上三个注解都有以下属性
- localProperty 主表字段的entity属性
- foreignProperty 副表字段的entity属性
- applySql 中间追加的sql语句，一般用于追加where，调用方法为LambdaQueryWrapper的apply方法
- lastSql 末尾追加的sql语句，调用LambdaQueryWrapper的last方法

##### 其中，@ManyBindMany多出以下几个属性
- linkModel 中间表模型类
- linkLocalProperty 中间表链接主表的字段的entity属性，如何属性名和localProperty，可以不设置
- linkForeignProperty 中间表链接副表的字段的entity属性，如何属性名和foreignProperty，可以不设置
- linkApplySql 中间表的中间追加的sql语句，同applySql，不过是用于中间表过滤
- linkLastSql 中间表末尾追加sql语句，同lastSql，作用于中间表过滤
- iterateLinkMethod 主表entity可以设置的迭代器方法，接收中间表过滤的List

###### 注意，被关联模型（entity）必须是mybatis-plus的Model的子孙类

### 查询注入

#### 一对一查询bindOne（一个用户对应一个部门）
先在UserVO添加@BindOne注解绑定关联方式
```java
@Data
@NoArgsConstructor
public class UserVO extends User {
    @BindOne(localProperty = "deptId", foreignProperty = "id")
    private Dept dept;
}
```
service中进行绑定
```java
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    /**
     * 查询单个用户信息
     */
    @Override
    public UserVO getOneUser(Integer userId) {
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery(User.class)
                .eq(User::getId, userId);
        UserVO userVo = EntityUtils.toObj(getOne(wrapper), UserVO::new);
        // 此处进行绑定操作
        Relations.with(userVo).bindOne(UserVO::getDept).end();
        return userVo;
    }
    
    /**
     * 批量查询用户信息
     */
    @Override
    public List<UserVO> getUserByList() {
        // 先查询用户信息（表现形式为列表）
        List<User> user = list(Wrappers.emptyWrapper());
        List<UserVO> userVos = user.stream().map(UserVO::new).collect(toList());
        // 此处进行绑定操作
        Relations.with(userVos).bindOne(UserVO::getDept).end();
        return userVos;
    }
}
```

#### 一对多查询bindMany（其中一个部门有多个用户）
先在模型进行绑定@BindMany注解
```java
@Data
public class DeptVO extends Dept {
    @BindMany(localProperty = "id", foreignProperty = "deptId")
    private List<User> users;
    public DeptVO(Dept dept) {
        super(dept);
    }
}
```
service中进行绑定
```java
/**
 * @author explore
 * @since 2021/05/24 11:09
 **/
@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements IDeptService {
    /**
     * 查询单个部门
     */
    @Override
    public DeptVO getOneDept(Integer deptId) {
        LambdaQueryWrapper<Dept> wrapper = Wrappers.lambdaQuery(Dept.class).eq(Dept::getId, deptId);
        DeptVO deptVo = EntityUtils.toObj(getOne(wrapper), DeptVO::new);
        // 此处进行绑定操作
        Relations.with(deptVo).bindMany(DeptVO::getUsers).end();
        return deptVo;
    }
    /**
     * 查询多个部门
     */
    @Override
    public List<DeptVO> getDeptByList() {
        List<DeptVO> deptVos = EntityUtils.toList(list(Wrappers.emptyWrapper()), DeptVO::new);
        // 此处进行绑定操作
        Relations.with(deptVos).bindMany(DeptVO::getUsers).end();
        return deptVos;
    }
}
```

#### 多对多查询ManyBindMany (每个用户拥有多项技能)
先在模型进行绑定@ManyBindMany注解
```
@Data
@NoArgsConstructor
public class UserVO extends User {
    @ManyBindMany(
            localProperty = "id",
            foreignProperty = "id",
            linkModel = UserSkillRelation.class,
            linkLocalProperty = "userId",
            linkForeignProperty = "skillId",
            iterateLinkMethod = "setUserSkillScope"
    )
    private List<SkillVO> skills;
    /**
     * 中间表迭代器 relations的size总是保持和skills的size一样
     * @param relations
     */
    public void setUserSkillScope(List<UserSkillRelation> relations) {
        for (int i = 0; i < relations.size(); i++) {
            this.skills.get(i).setScore(relations.get(i).getScore());
        }
    }
}
```
service中进行绑定
```java
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    /**
     * 查询单个用户信息
     */
    @Override
    public UserVO getOneUser(Integer userId) {
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery(User.class)
                .eq(User::getId, userId);
        UserVO userVo = EntityUtils.toObj(getOne(wrapper), UserVO::new);
        // 此处进行绑定操作
        Relations.with(userVo).manyBindMany(UserVO::getSkills).end();
        return userVo;
    }
    
    /**
     * 批量查询用户信息
     */
    @Override
    public List<UserVO> getUserByList() {
        // 先查询用户信息（表现形式为列表）
        List<User> user = list(Wrappers.emptyWrapper());
        List<UserVO> userVos = user.stream().map(UserVO::new).collect(toList());
        // 此处进行绑定操作
        Relations.with(userVos).manyBindMany(UserVO::getSkills).end();
        return userVos;
    }
}
```

##### 多对多绑定特有属性ManyBindMany
@ManyBindMany可以实现多对多绑定，并且实现了IManyBindHandler接口的linkQuery方法，可以对中间表的query进行过滤查询
用户和技能表示多对多关联，在manyBindMany后，可以进行linkQuery，此方法传递的是中间表的LambdaQueryWrapper，可以对中间表进行过滤检索
```java
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
@Override
    public List<UserVO> getUserByList() {
        List<User> user = list(Wrappers.emptyWrapper());
        List<UserVO> userVos = user.stream().map(UserVO::new).collect(toList());
        // 此步骤可以有多个
        Relations.with(userVos)
                .manyBindMany(UserVO::getSkills)
                .linkQuery((LambdaQueryWrapper<UserSkillRelation> wrapper) -> {
                    wrapper.gt(UserSkillRelation::getScore, 90);
                })
                .end();
        return userVos;
    }
}
```
##### iterateLinkMethod迭代器
@ManyBindMany注解有一个iterateLinkMethod迭代器属性，可以传入主模型的一个方法，对中间表进行迭代操作，详情可以查阅上面的模型代码，让模型可以获取到中间表的数据进行一些操作

#### 关联表追加query
@BindOne、@BindMany、@ManyBindMany三个注解除了在注解时可以追加applySql和lastSql外，还可以在service中进行绑定，如下

```java
@Service
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements IDeptService {
    @Override
    public List<DeptVO> getDeptByList() {
        // 按条件查询部门信息
        List<DeptVO> deptVos = EntityUtils.toList(list(Wrappers.emptyWrapper()), DeptVO::new);
        // query方法可以过滤副表user的ID为1
        Relations.with(deptVos)
                .bindMany(DeptVO::getUsers)
                .query(wrapper -> wrapper.eq(User::getUserId, 1))
                .end();
        return deptVos;
    }
}
```

#### 深度绑定
IBinder提供一个deepBinder的方法，可以将副表的绑定器继续绑定新的关联属性，实现递归绑定操作

模型关系，一个用户有多项技能关联表，每个技能关联表UserSkillVO又关联了一个技能属性Skill
```java
@Data
@NoArgsConstructor
public class UserVO extends User {
		@BindMany(localProperty = "id", foreignProperty = "userId")
    private List<UserSkillVO> userSkills;
}
```
```java
@Data
public class UserSkillVO extends UserSkillRelation {
    @BindOne(localProperty = "skillId", foreignProperty = "id")
    private Skill skill;
}
```
具体业务代码，用户模型先bindMany检索出所有的关联数据，再通过deepWith获得副表的Binder，追加绑定副表的关系
```java
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
@Override
    public UserVO getOneUser(Integer userId) {
        LambdaQueryWrapper<User> wrapper = Wrappers.lambdaQuery(User.class)
                .eq(User::getId, userId);
        UserVO userVo = EntityUtils.toObj(getOne(wrapper), UserVO::new);
        Relations.with(userVo)
                .bindMany(UserVO::getUserSkills)
                .deepWith(binder -> {
                    binder.bindOne(UserSkillVO::getSkill)
                            .end();
                })
                .end();
        return userVo;
    }
}
```

### 具体实现

具体包含以下几个步骤:
1. 解析@RelationScan的包路劲下的所有类，并扫描到含有@BindOne、@BindMany、@ManyBindMany的注解信息
2. 将注解信息放入缓存器RelationCache中缓存起来
3. 当业务层调用Relations.with方法时候，返回一个绑定器IBinder
4. IBinder绑定器根据注入数据的类型和属性返回一个具体的关联处理器IHandler
5. 关联处理器IHander进行副表或中间表的queryWrapper构建和数据查询，并组装好返回业务层
6. 如果需要绑定多个关联属性，可以继续调用IBinder的其他bind方法构建新的属性绑定器

TODO:  示意图


### 常见问题
- 出现java.lang.ClassCastException，可能是项目使用了spring-boot-devtools，可以在src/main/resources/META-INF/spring-devtools.
  properties添加如下，或者移除spring-boot-devtools
```shell
restart.include.relations=/io.gitee.zhuyunlong2018.*.jar
```

### 参考资料

- 扫描包路劲注解使用 mprelation：https://github.com/dreamyoung/mprelation.git
- 测试用数据结构直接使用 mybatisplus-joinquery：https://gitee.com/java-spring-demo/mybatisplus-joinquery.git
- 部分代码参考：https://gitee.com/xiaokedamowang/bilibili


#### deploy
mvn clean deploy -pl mybatis-plus-relations-core -am
