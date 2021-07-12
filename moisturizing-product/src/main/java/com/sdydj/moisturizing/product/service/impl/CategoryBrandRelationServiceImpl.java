package com.sdydj.moisturizing.product.service.impl;


import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.sdydj.moisturizing.product.dao.BrandDao;
import com.sdydj.moisturizing.product.dao.CategoryDao;
import com.sdydj.moisturizing.product.entity.BrandEntity;
import com.sdydj.moisturizing.product.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sdydj.common.utils.PageUtils;
import com.sdydj.common.utils.Query;

import com.sdydj.moisturizing.product.dao.CategoryBrandRelationDao;
import com.sdydj.moisturizing.product.entity.CategoryBrandRelationEntity;
import com.sdydj.moisturizing.product.service.CategoryBrandRelationService;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {
    @Autowired
    BrandDao brandDao;

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    CategoryBrandRelationDao relationDao;

    @Autowired
    BrandService brandService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveDetail(CategoryBrandRelationEntity categoryBrandRelation) {
        Long brandId = categoryBrandRelation.getBrandId();
        Long catelogId = categoryBrandRelation.getCatelogId();

        categoryBrandRelation.setBrandName(brandDao.selectById(brandId).getName());
        categoryBrandRelation.setCatelogName(categoryDao.selectById(catelogId).getName());
        this.save(categoryBrandRelation);
    }

    @Override
    public void updataBrand(Long brandId, String name) {
        CategoryBrandRelationEntity categoryBrandRelationEntity = new CategoryBrandRelationEntity();
        categoryBrandRelationEntity.setBrandId(brandId);
        categoryBrandRelationEntity.setBrandName(name);
        this.update(categoryBrandRelationEntity,new UpdateWrapper<CategoryBrandRelationEntity>().eq("brand_id",brandId));
    }

    @Override
    public void updataCategory(Long catId, String name) {
        //CategoryBrandRelationEntity categoryBrandRelationEntity = new CategoryBrandRelationEntity();
      this.baseMapper.updateCategory(catId,name);
    }

    @Override
    public List<BrandEntity> getBrands(Long catId) {
        List<CategoryBrandRelationEntity> catelogId = relationDao.selectList(new QueryWrapper<CategoryBrandRelationEntity>().eq("catelog_id", catId));
        List<BrandEntity> collect = catelogId.stream().map((item) -> {
            Long brandId = item.getBrandId();
            BrandEntity byId = brandService.getById(brandId);
            return byId;
        }).collect(Collectors.toList());

        return collect;
    }

}