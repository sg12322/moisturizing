package com.sdydj.moisturizing.product;



import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

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
 */
@EnableFeignClients("com.sdydj.moisturizing.product.feign")
@EnableDiscoveryClient
@SpringBootApplication
public class  MoisturizingProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoisturizingProductApplication.class, args);
    }

}
