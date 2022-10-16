package com.febs.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication(exclude = {RedisAutoConfiguration.class})
public class FebsGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(FebsGatewayApplication.class, args);
    }

}
