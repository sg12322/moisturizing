package com.sdydj.moisturizing.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sdydj.common.utils.PageUtils;
import com.sdydj.moisturizing.product.entity.BrandEntity;
import com.sdydj.moisturizing.product.entity.CategoryBrandRelationEntity;

import java.util.List;
import java.util.Map;

/**
 * 品牌分类关联
 *
 * @author sdy
 * @email sdy@gmail.com
 * @date 2021-04-17 02:14:59
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveDetail(CategoryBrandRelationEntity categoryBrandRelation);

    void updataBrand(Long brandId, String name);

    void updataCategory(Long catId, String name);

    List<BrandEntity> getBrands(Long catId);

}

