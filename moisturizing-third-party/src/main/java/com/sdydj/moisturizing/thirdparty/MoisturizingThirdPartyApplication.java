package com.sdydj.moisturizing.thirdparty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class MoisturizingThirdPartyApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoisturizingThirdPartyApplication.class, args);
    }

}
