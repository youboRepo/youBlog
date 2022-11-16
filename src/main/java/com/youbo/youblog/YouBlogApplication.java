package com.youbo.youblog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.youbo.youblog.**.mapper")
public class YouBlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(YouBlogApplication.class, args);
    }
}