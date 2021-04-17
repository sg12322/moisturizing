package com.sdydj.moisturizing.coupon.dao;

import com.sdydj.moisturizing.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author sdydj
 * @email sdy@gmail.com
 * @date 2021-04-18 03:06:46
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
