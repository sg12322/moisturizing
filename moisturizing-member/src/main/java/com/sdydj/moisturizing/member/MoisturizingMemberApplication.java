package com.sdydj.moisturizing.member;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


/*
    远程调用
      引入open fen
      编写 远程接口
        声明每一个方法都是调用哪个远程服务的那个请求
       开启远程调用功能
 */
@EnableFeignClients(basePackages = "com.sdydj.moisturizing.member.feign")
@EnableDiscoveryClient
@SpringBootApplication
public class MoisturizingMemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoisturizingMemberApplication.class, args);
    }

}
