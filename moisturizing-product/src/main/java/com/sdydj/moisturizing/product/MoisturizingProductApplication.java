package com.sdydj.moisturizing.product;



import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 *整合mybatis-plus
 * 1导入依赖
 * 2配置
 *   配置数据源
 *      导入驱动
 *       在application.yml配置数据源相关信息
 *   配置mybatis-plus
 *         使用mapperscan dao位置
 *         sql映射文件位置
 *
 *
 *   逻辑删除
 *       1配置com.baomidou.mybatisplus.core.config.GlobalConfig$DbConfig
 *       2实体类字段上加上@TableLogic注解
 *
 */
@EnableDiscoveryClient
@MapperScan("com.sdydj.moisturizing.product.dao")
@SpringBootApplication
public class MoisturizingProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoisturizingProductApplication.class, args);
    }

}
