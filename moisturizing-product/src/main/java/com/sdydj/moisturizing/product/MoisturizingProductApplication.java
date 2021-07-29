package com.sdydj.moisturizing.product;



import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 *整合mybatis-plus
 * 1导入依赖
` * 2配置
` *   配置数据源
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
 *    JSR303校验
 *      给 Bean添加校验注解  javax.validation.constraints.
 *       开启校验功能@valid
 *       BindingResult 处理异常
 *       分组校验
 *           (message = "品牌名必须提交",groups = {AddGroup.class,UpdateGroup.class})
 *             给 Bean添加分组
 *           @Validated({AddGroup.class}
 *
 *           没有指定分组的校验注解@NotEmpty 在分组校验时不生效
 *           只会在 @Validated生效
 *        自定义校验
 *              编写一个校验注解
 *              编写一个自定义校验器 ConstraintValidator
 *              关联自定义校验器和注解
 *              @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
 * @Retention(RUNTIME)
 * @Documented
 * @Constraint(validatedBy = {ListValueConstraintValidator.class [可以指定多个不同的校验器 ]})
 *
 *
 *   统一的异常处理
 *      编写异常处理类 使用@ControllerAdvice
 *     使用@ExceptionHandler 标注方法 处理异常
 *
 *
 *   模板引擎
 *      thymnleaf-starter 关闭缓存
 *      静态资源放在static文件夹下就可以按照路径直接访问
 *      页面放在templates下，直接访问
 *      springboot访问项目的时候默认会找index
 *      页面修改不重启服务器实时更新（引入dev—tolls  修改完页面 ctrl+f9(buildproject)）  代码配置推荐重启
*
 *
 *
 *   整合redis
 *      引入 data-redis-starter
 *      简单配置redis的host port信息
 *      使用springboot自动配置好的StringRedisTemplate来操作redis
 *       redis->map   redis存放数据按照key 数据值 value
 *
 *
 *   整合redisson作为分布式锁等功能框架
 *       引入依赖
 *         <dependency>
 *             <groupId>org.redisson</groupId>
 *             <artifactId>redisson</artifactId>
 *             <version>3.16.0</version>
 *         </dependency>
 *
 * 整合Springcache简化缓存
 *      引入依赖 spring-boot-starter-cache     spring-boot-starter-data-redis
 *      写配置  自动配置了哪些    cacheAutoConfigration会导入RedisCacheConfiguration
 *      配置使用redis作为缓存
 *      测试使用缓存
 *          @Cacheable: Triggers cache population. 触发将数据保存到缓存的操作
 *          @CacheEvict: Triggers cache eviction.
 *          @CachePut: Updates the cache without interfering with the method execution.
 *          @Caching: Regroups multiple cache operations to be applied on a method.
 *          @CacheConfig: Shares some common cache-related settings at class-level.
 *
 */
@EnableFeignClients("com.sdydj.moisturizing.product.feign")
@EnableDiscoveryClient
@SpringBootApplication
public class  MoisturizingProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoisturizingProductApplication.class, args);
    }

}
