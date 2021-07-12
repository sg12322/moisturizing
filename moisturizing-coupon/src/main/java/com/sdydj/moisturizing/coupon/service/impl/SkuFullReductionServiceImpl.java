package com.sdydj.moisturizing.coupon.service.impl;

import com.sdydj.common.to.MemberPrice;
import com.sdydj.common.to.SkuReductionTo;
import com.sdydj.moisturizing.coupon.entity.MemberPriceEntity;
import com.sdydj.moisturizing.coupon.entity.SkuLadderEntity;
import com.sdydj.moisturizing.coupon.service.MemberPriceService;
import com.sdydj.moisturizing.coupon.service.SkuLadderService;
import com.sdydj.moisturizing.coupon.service.SpuBoundsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sdydj.common.utils.PageUtils;
import com.sdydj.common.utils.Query;

import com.sdydj.moisturizing.coupon.dao.SkuFullReductionDao;
import com.sdydj.moisturizing.coupon.entity.SkuFullReductionEntity;
import com.sdydj.moisturizing.coupon.service.SkuFullReductionService;


@Service("skuFullReductionService")
public class SkuFullReductionServiceImpl extends ServiceImpl<SkuFullReductionDao, SkuFullReductionEntity> implements SkuFullReductionService {

    @Autowired
    SkuLadderService skuLadderService;


    @Autowired
    MemberPriceService memberPriceService;

    @Autowired
    SpuBoundsService spuBoundsService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuFullReductionEntity> page = this.page(
                new Query<SkuFullReductionEntity>().getPage(params),
                new QueryWrapper<SkuFullReductionEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSkuReduction(SkuReductionTo skuReductionTo) {
        //保存满减打折  会员价  sms_sku_ladder   sms_sku_full_reduction   sms_member_price

         //sms_sku_ladder
        SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
         skuLadderEntity.setSkuId(skuReductionTo.getSkuId());
         skuLadderEntity.setDiscount(skuReductionTo.getDiscount());
         skuLadderEntity.setFullCount(skuReductionTo.getFullCount());
         skuLadderEntity.setAddOther(skuReductionTo.getCountStatus());
         if(skuLadderEntity.getFullCount()>0){
             skuLadderService.save(skuLadderEntity);
         }


         //sms_sku_full_reduction
        SkuFullReductionEntity skuFullReductionEntity = new SkuFullReductionEntity();
        BeanUtils.copyProperties(skuReductionTo,skuFullReductionEntity);
        //skuFullReductionEntity.setAddOther(skuReductionTo.getCountStatus());
        if(skuFullReductionEntity.getFullPrice().compareTo(new BigDecimal("0"))==1){
            this.save(skuFullReductionEntity);
        }


        //sms_member_price
        List<MemberPrice> memberPrice = skuReductionTo.getMemberPrice();
        List<MemberPriceEntity> collect = memberPrice.stream().map((item) -> {
            MemberPriceEntity memberPriceEntity = new MemberPriceEntity();
            memberPriceEntity.setSkuId(skuReductionTo.getSkuId());
            memberPriceEntity.setMemberLevelId(item.getId());
            memberPriceEntity.setMemberLevelName(item.getName());
            memberPriceEntity.setMemberPrice(item.getPrice());
            memberPriceEntity.setAddOther(1);
            return memberPriceEntity;
        }).filter((item)->{
            return item.getMemberPrice().compareTo(new BigDecimal("0"))==1;
        }).collect(Collectors.toList());

        memberPriceService.saveBatch(collect);



    }

}