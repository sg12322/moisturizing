package com.sdydj.moisturizing.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.sdydj.moisturizing.product.dao.CategoryBrandRelationDao;
import com.sdydj.moisturizing.product.service.CategoryBrandRelationService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sdydj.common.utils.PageUtils;
import com.sdydj.common.utils.Query;

import com.sdydj.moisturizing.product.dao.BrandDao;
import com.sdydj.moisturizing.product.entity.BrandEntity;
import com.sdydj.moisturizing.product.service.BrandService;
import org.springframework.transaction.annotation.Transactional;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {
    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;



    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String key = (String)params.get("key");
        QueryWrapper<BrandEntity> objectQueryWrapper = new QueryWrapper<BrandEntity>();
        if(!StringUtils.isEmpty(key)){
            objectQueryWrapper.eq("brand_id",key).or().like("name",key);
        }

        IPage<BrandEntity> page = this.page(
                new Query<BrandEntity>().getPage(params),
                objectQueryWrapper
        );

        return new PageUtils(page);
    }
    @Transactional
    @Override
    public void updateDetail(BrandEntity brand) {
        this.updateById(brand);
        if(!StringUtils.isEmpty(brand.getName())){
            categoryBrandRelationService.updataBrand(brand.getBrandId(),brand.getName());
        }
    }

}