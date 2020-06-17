package com.chen.stencil;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.chen.stencil.mapper")
public class StencilApplication {

    public static void main(String[] args) {
        SpringApplication.run(StencilApplication.class, args);
    }

}
