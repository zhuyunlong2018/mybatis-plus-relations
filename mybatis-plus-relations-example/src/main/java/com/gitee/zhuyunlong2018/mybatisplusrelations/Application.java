package com.gitee.zhuyunlong2018.mybatisplusrelations;

import com.gitee.zhuyunlong2018.mybatisplusrelations.annotations.RelationScan;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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