package com.sdydj.moisturizing.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sdydj.common.utils.PageUtils;
import com.sdydj.moisturizing.ware.entity.PurchaseEntity;
import com.sdydj.moisturizing.ware.vo.MergeVo;
import com.sdydj.moisturizing.ware.vo.PurchaseDoneVo;

import java.util.List;
import java.util.Map;

/**
 * 采购信息
 *
 * @author sdydj
 * @email sdy@gmail.com
 * @date 2021-04-18 03:50:05
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils queryPageUnreceive(Map<String, Object> params);

    void mergePurchase(MergeVo vo);

    void received(List<Long> ids);

    void done(PurchaseDoneVo doneVo);

}

