package com.sdydj.moisturizing.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sdydj.common.utils.PageUtils;
import com.sdydj.moisturizing.product.entity.CategoryEntity;
import com.sdydj.moisturizing.product.vo.Catelog2Vo;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author sdy
 * @email sdy@gmail.com
 * @date 2021-04-17 02:14:59
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<CategoryEntity> listWithTree();


    void removeMenuByIds(List<Long> asList);

    /**
     * 找到 catelogId的完整路径
     * [f/z/s]
     * @param catelogId
     * @return
     */
    Long[] findCatelogPath(Long catelogId);

    /***
     * 更新关联数据
     * @param category
     */
    void updateDetail(CategoryEntity category);

    List<CategoryEntity> getLeavel1Categotys();

    Map<String, List<Catelog2Vo>> getCatalogJson();
}

