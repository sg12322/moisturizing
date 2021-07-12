package com.sdydj.moisturizing.ware.dao;

import com.sdydj.moisturizing.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 商品库存
 * 
 * @author sdydj
 * @email sdy@gmail.com
 * @date 2021-04-18 03:50:05
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {

    void addStock(@Param("skuId") Long skuId, @Param("wareId") Long wareId, @Param("skuNum") Integer skuNum);

    Long getSkusHasStock(@Param("skuId") Long skuId);

}
