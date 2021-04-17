package com.sdydj.moisturizing.product;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sdydj.moisturizing.product.entity.BrandEntity;
import com.sdydj.moisturizing.product.service.BrandService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class MoisturizingProductApplicationTests {

    @Autowired
    BrandService brandService;


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
        List<BrandEntity> list = brandService.list(new QueryWrapper<BrandEntity>().eq("brand_id", 2L));
        list.forEach((item)->{
            System.out.println(item);
        });


    }
}