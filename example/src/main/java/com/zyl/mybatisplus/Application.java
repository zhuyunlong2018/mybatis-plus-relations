package com.zyl.mybatisplus;

import com.zyl.mybatisplus.relations.annotations.RelationsScan;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.zyl.mybatisplus.**.mapper")
@RelationsScan({"com.zyl.mybatisplus.*.vo", "com.zyl.mybatisplus.entity"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}