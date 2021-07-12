package com.sdydj.moisturizing.ware.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.sdydj.moisturizing.ware.vo.MergeVo;
import com.sdydj.moisturizing.ware.vo.PurchaseDoneVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sdydj.moisturizing.ware.entity.PurchaseEntity;
import com.sdydj.moisturizing.ware.service.PurchaseService;
import com.sdydj.common.utils.PageUtils;
import com.sdydj.common.utils.R;



/**
 * 采购信息
 *
 * @author sdydj
 * @email sdy@gmail.com
 * @date 2021-04-18 03:50:05
 */
@RestController
@RequestMapping("ware/purchase")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;

    ///ware/purchase/done
    @PostMapping("/done")
    public R finish(@RequestBody PurchaseDoneVo doneVo){
        purchaseService.done(doneVo);
        return  R.ok();
    }

    @PostMapping("/received")
    public R received(@RequestBody List<Long> ids){
        purchaseService.received(ids);
        return  R.ok();
    }
    ///ware/purchase/unreceive/list
    ///purchase/merge
    @PostMapping("/merge")
    public R merge(@RequestBody MergeVo vo){
        purchaseService.mergePurchase(vo);
     return  R.ok();
    }

    /**
     * /unreceive/list
     * 请求参数
     * @param params
     * @return
     */
    @RequestMapping("/unreceive/list")
    // @RequiresPermissions("ware:purchase:list")
    public R unreceive(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPageUnreceive(params);

        return R.ok().put("page", page);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
   // @RequiresPermissions("ware:purchase:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("ware:purchase:info")
    public R info(@PathVariable("id") Long id){
		PurchaseEntity purchase = purchaseService.getById(id);

        return R.ok().put("purchase", purchase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("ware:purchase:save")
    public R save(@RequestBody PurchaseEntity purchase){
        purchase.setCreateTime(new Date());
        purchase.setUpdateTime(new Date());
		purchaseService.save(purchase);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("ware:purchase:update")
    public R update(@RequestBody PurchaseEntity purchase){
		purchaseService.updateById(purchase);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("ware:purchase:delete")
    public R delete(@RequestBody Long[] ids){
		purchaseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
