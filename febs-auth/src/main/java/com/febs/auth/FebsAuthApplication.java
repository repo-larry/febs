package com.febs.auth;

import com.febs.common.annotation.EnableFebsAuthExceptionHandler;
import com.febs.common.annotation.EnableFebsLettuceRedis;
import com.febs.common.annotation.EnableFebsServerProtect;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
@EnableFebsServerProtect
@EnableFebsAuthExceptionHandler
@EnableFebsLettuceRedis
@MapperScan("com.febs.auth.mapper")
public class FebsAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(FebsAuthApplication.class, args);
    }

}
