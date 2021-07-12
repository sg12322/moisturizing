package com.sdydj.moisturizing.ware.service.impl;

import com.sdydj.moisturizing.ware.entity.PurchaseEntity;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sdydj.common.utils.PageUtils;
import com.sdydj.common.utils.Query;

import com.sdydj.moisturizing.ware.dao.PurchaseDetailDao;
import com.sdydj.moisturizing.ware.entity.PurchaseDetailEntity;
import com.sdydj.moisturizing.ware.service.PurchaseDetailService;


@Service("purchaseDetailService")
public class PurchaseDetailServiceImpl extends ServiceImpl<PurchaseDetailDao, PurchaseDetailEntity> implements PurchaseDetailService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        /**
         *  key: '华为',//检索关键字
         *    status: 0,//状态
         *    wareId: 1,//仓库id
         */
        QueryWrapper<PurchaseDetailEntity> queryWrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)){
            queryWrapper.and((w)->{
                w.eq("purchase_id",key).or().like("sku_id",key);
            });
        }
        String status = (String) params.get("status");
        if (!StringUtils.isEmpty(status)){
            queryWrapper.eq("status",status);

        }
        String wareId = (String) params.get("wareId");
        if (!StringUtils.isEmpty(wareId)){
            queryWrapper.eq("ware_id",wareId);
        }
        IPage<PurchaseDetailEntity> page = this.page(
                new Query<PurchaseDetailEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public List<PurchaseDetailEntity> ListDetailByIdPurchase(Long id) {
        List<PurchaseDetailEntity> purchase = this.list(new QueryWrapper<PurchaseDetailEntity>().eq("purchase_id", id));

        return purchase;
    }


}