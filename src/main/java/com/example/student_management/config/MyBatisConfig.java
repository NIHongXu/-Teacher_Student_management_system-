package com.example.student_management.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "com.example.student_management.Mapper",
         annotationClass = org.apache.ibatis.annotations.Mapper.class)
public class MyBatisConfig {

}