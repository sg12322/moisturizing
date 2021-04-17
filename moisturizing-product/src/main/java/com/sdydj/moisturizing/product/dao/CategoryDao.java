package com.sdydj.moisturizing.product.dao;

import com.sdydj.moisturizing.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author sdy
 * @email sdy@gmail.com
 * @date 2021-04-17 02:14:59
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
