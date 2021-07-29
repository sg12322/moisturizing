package com.sdydj.moisturizing.product;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sdydj.moisturizing.product.entity.BrandEntity;
import com.sdydj.moisturizing.product.service.AttrGroupService;
import com.sdydj.moisturizing.product.service.BrandService;
import com.sdydj.moisturizing.product.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class MoisturizingProductApplicationTests {

    @Autowired
    BrandService brandService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedissonClient redissonClient;
    @Test
    public void reidsson(){
        System.out.println(redissonClient);
    }

    @Test
    public void testRedis(){

        ValueOperations<String, String> forValue = stringRedisTemplate.opsForValue();
        //保存
        forValue.set("hello","world"+ UUID.randomUUID().toString());
        //查询
        String hello = forValue.get("hello");
        System.out.println(hello);

    }


    @Test
    public void testFindPareent(){

        Long[] catelogPath = categoryService.findCatelogPath(225L);
//
        log.info("完整路径，{}", Arrays.asList(catelogPath));
    }
    @Test
    public void contextLoads() {

/*
        BrandEntity brandEntity = new BrandEntity();
        brandEntity.setBrandId(2L);
        brandEntity.setDescript("红米");
   */
/*     brandEntity.setDescript("华为");


        brandEntity.setName("华为");
        brandService.save(brandEntity);
        System.out.println("保存成功....");*//*

        brandService.updateById(brandEntity);
*/
//        List<BrandEntity> list = brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id", 2L));
//        list.forEach((item)->{
//            System.out.println(item);
//        });


    }


}