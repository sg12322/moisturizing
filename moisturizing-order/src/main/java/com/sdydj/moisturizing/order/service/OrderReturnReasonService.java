package com.sdydj.moisturizing.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sdydj.common.utils.PageUtils;
import com.sdydj.moisturizing.order.entity.OrderReturnReasonEntity;

import java.util.Map;

/**
 * ้่ดงๅๅ 
 *
 * @author sdydj
 * @email sdy@gmail.com
 * @date 2021-04-18 03:41:47
 */
public interface OrderReturnReasonService extends IService<OrderReturnReasonEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

