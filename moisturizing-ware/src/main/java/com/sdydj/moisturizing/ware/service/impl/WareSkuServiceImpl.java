package com.sdydj.moisturizing.ware.service.impl;

import com.sdydj.common.utils.R;
import com.sdydj.moisturizing.ware.feign.ProductFeignService;
import com.sdydj.moisturizing.ware.vo.SkuHasStockVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sdydj.common.utils.PageUtils;
import com.sdydj.common.utils.Query;

import com.sdydj.moisturizing.ware.dao.WareSkuDao;
import com.sdydj.moisturizing.ware.entity.WareSkuEntity;
import com.sdydj.moisturizing.ware.service.WareSkuService;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {
   @Autowired
    WareSkuDao wareSkuDao;
   @Autowired
    ProductFeignService productFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        /**
         * skuId:
         * wareId:
         */
        QueryWrapper<WareSkuEntity> queryWrapper = new QueryWrapper<>();
        String skuId = (String) params.get("skuId");
        if(!StringUtils.isEmpty(skuId)){
            queryWrapper.eq("sku_id",skuId);
        }
        String wareId = (String) params.get("wareId");
        if(!StringUtils.isEmpty(wareId)){
            queryWrapper.eq("ware_id",wareId);
        }
        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public void addStock(Long skuId, Long wareId, Integer skuNum) {
        List<WareSkuEntity> list = this.list(new QueryWrapper<WareSkuEntity>().eq("sku_id", skuId).eq("ware_id", wareId));
        if(list==null || list.size()==0){
            WareSkuEntity wareSkuEntity = new WareSkuEntity();
            wareSkuEntity.setSkuId(skuId);
            wareSkuEntity.setWareId(wareId);
            wareSkuEntity.setStock(skuNum);
            wareSkuEntity.setStockLocked(0);
            //TODO 远程查询sku名字
            //自己catch异常
            //TODO 怎样让异常出现不回滚？
            try{
                R skuInfo = productFeignService.getSkuInfo(skuId);
              if( skuInfo.getCode()==0) {
                  Map<String,Object> info = (Map<String,Object>)skuInfo.get("skuInfo");
                  wareSkuEntity.setSkuName((String)info.get("skuName"));
              }
            }catch (Exception e){

            }
      wareSkuDao.insert(wareSkuEntity);
        }else {
            wareSkuDao.addStock(skuId,wareId,skuNum);
        }

    }

    @Override
    public List<SkuHasStockVo> getSkusHasStock(List<Long> skuIds) {


        List<SkuHasStockVo> collect = skuIds.stream().map((skuId) -> {
            SkuHasStockVo skuHasStockVo = new SkuHasStockVo();
            //查询当前sku的总库存量
            // SELECT SUM(stock-stock_locked) FROM `wms_ware_sku` WHERE sku_id=1;
            Long count=baseMapper.getSkusHasStock(skuId);
            skuHasStockVo.setSkuId(skuId);
             skuHasStockVo.setHasStock(count==null?false:count>0);
            return skuHasStockVo;
        }).collect(Collectors.toList());
        return collect;
    }


}