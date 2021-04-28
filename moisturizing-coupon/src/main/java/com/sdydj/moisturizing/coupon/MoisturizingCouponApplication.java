package com.sdydj.moisturizing.coupon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


/****
 *  nacos配置中心
 *    引入依赖
 *    创建bootstrap.properties
 *      spring.application.name=
 *      spring.cloud.nacos.config.server-addr=
 *    nacos配置中心添加配置
 *    动态获取配置
 *  @RefreshScope
 * @value（"${user.name}"）
 * 优先使用配置中心
 *
 *
 * 命名空间  配置隔离
 *      默认public 默认新增在public空间
 *          开发 测试 生产 利用命名空间 做环境隔离 spring.cloud.nacos.config.namespace=命名空间id
 *      每一个微服务互相隔离 只加载自己命名空间的配置
 * 配置集
 *      所有的配置的集合
 * 配置集ID
 *      类似于配置文件名  Data ID
 * 配置分组
 *      默认所有配置集属于DEFAULT_GROUP
 *
 *
 * 每个微服务创建自己的命名空间 使用配置分组区分环境 test prod dev
 *
 *
 *
 *
 * 同时加载多个配置集
 *      微服务任何配置都能放在配置中心 中
 *      只需在bootstrap.properties说明加载哪些配置文件
 *    以前springboot任何方法能从配置中心获取
 *
 *
 *
 *
 */

@EnableDiscoveryClient
@SpringBootApplication
public class MoisturizingCouponApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoisturizingCouponApplication.class, args);
    }

}
