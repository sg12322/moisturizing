package com.sdydj.moisturizing.coupon.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sdydj.moisturizing.coupon.entity.CouponSpuCategoryRelationEntity;
import com.sdydj.moisturizing.coupon.service.CouponSpuCategoryRelationService;
import com.sdydj.common.utils.PageUtils;
import com.sdydj.common.utils.R;



/**
 * 优惠券分类关联
 *
 * @author sdydj
 * @email sdy@gmail.com
 * @date 2021-04-18 03:06:46
 */
@RestController
@RequestMapping("coupon/couponspucategoryrelation")
public class CouponSpuCategoryRelationController {
    @Autowired
    private CouponSpuCategoryRelationService couponSpuCategoryRelationService;

    /**
     * 列表
     */
    @RequestMapping("/list")
   // @RequiresPermissions("coupon:couponspucategoryrelation:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = couponSpuCategoryRelationService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("coupon:couponspucategoryrelation:info")
    public R info(@PathVariable("id") Long id){
		CouponSpuCategoryRelationEntity couponSpuCategoryRelation = couponSpuCategoryRelationService.getById(id);

        return R.ok().put("couponSpuCategoryRelation", couponSpuCategoryRelation);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("coupon:couponspucategoryrelation:save")
    public R save(@RequestBody CouponSpuCategoryRelationEntity couponSpuCategoryRelation){
		couponSpuCategoryRelationService.save(couponSpuCategoryRelation);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("coupon:couponspucategoryrelation:update")
    public R update(@RequestBody CouponSpuCategoryRelationEntity couponSpuCategoryRelation){
		couponSpuCategoryRelationService.updateById(couponSpuCategoryRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("coupon:couponspucategoryrelation:delete")
    public R delete(@RequestBody Long[] ids){
		couponSpuCategoryRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
