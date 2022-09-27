package com.febs.system;

import com.febs.common.annotation.EnableFebsAuthExceptionHandler;
import com.febs.common.annotation.EnableFebsServerProtect;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableDiscoveryClient
@SpringBootApplication(exclude = {RedisAutoConfiguration.class})
@EnableAspectJAutoProxy
@EnableFebsServerProtect
@EnableTransactionManagement
@EnableFebsAuthExceptionHandler
@MapperScan("com.febs.system.mapper")
public class FebsServerSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(FebsServerSystemApplication.class, args);
    }

}
