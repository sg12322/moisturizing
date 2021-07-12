package com.sdydj.moisturizing.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sdydj.common.utils.PageUtils;
import com.sdydj.moisturizing.ware.entity.PurchaseDetailEntity;
import com.sdydj.moisturizing.ware.entity.PurchaseEntity;

import java.util.List;
import java.util.Map;

/**
 * 
 *
 * @author sdydj
 * @email sdy@gmail.com
 * @date 2021-04-18 03:50:05
 */
public interface PurchaseDetailService extends IService<PurchaseDetailEntity> {

    PageUtils queryPage(Map<String, Object> params);


    List<PurchaseDetailEntity> ListDetailByIdPurchase(Long id);

}

