package com.sdydj.moisturizing.product;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sdydj.moisturizing.product.entity.BrandEntity;
import com.sdydj.moisturizing.product.service.AttrGroupService;
import com.sdydj.moisturizing.product.service.BrandService;
import com.sdydj.moisturizing.product.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class MoisturizingProductApplicationTests {

    @Autowired
    BrandService brandService;

    @Autowired
    CategoryService categoryService;

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