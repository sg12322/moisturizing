package com.sdydj.moisturizing.ware;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@EnableFeignClients("com.sdydj.moisturizing.ware.feign")
@EnableDiscoveryClient
@SpringBootApplication
public class MoisturizingWareApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoisturizingWareApplication.class, args);
    }

}
