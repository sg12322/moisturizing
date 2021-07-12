package com.sdydj.moisturizing.product.dao;

import com.sdydj.moisturizing.product.entity.SpuInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * spu信息
 * 
 * @author sdy
 * @email sdy@gmail.com
 * @date 2021-04-17 02:14:59
 */
@Mapper
public interface SpuInfoDao extends BaseMapper<SpuInfoEntity> {

    void updateUpdateStau(@Param("spuId") Long spuId, @Param("code") int code);

}
