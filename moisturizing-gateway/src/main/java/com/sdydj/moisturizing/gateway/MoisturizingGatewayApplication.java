package com.sdydj.moisturizing.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


/***
 * 1.开启服务注册发现
 *
 */
@EnableDiscoveryClient
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class MoisturizingGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoisturizingGatewayApplication.class, args);
    }

}
