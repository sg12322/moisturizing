package com.sdydj.moisturizing.order.dao;

import com.sdydj.moisturizing.order.entity.OrderSettingEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单配置信息
 * 
 * @author sdydj
 * @email sdy@gmail.com
 * @date 2021-04-18 03:41:47
 */
@Mapper
public interface OrderSettingDao extends BaseMapper<OrderSettingEntity> {
	
}
