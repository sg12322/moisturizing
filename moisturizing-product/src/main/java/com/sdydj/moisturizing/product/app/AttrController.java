package com.sdydj.moisturizing.product.app;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.sdydj.moisturizing.product.entity.ProductAttrValueEntity;
import com.sdydj.moisturizing.product.service.ProductAttrValueService;
import com.sdydj.moisturizing.product.vo.AttrRespVo;
import com.sdydj.moisturizing.product.vo.AttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sdydj.moisturizing.product.service.AttrService;
import com.sdydj.common.utils.PageUtils;
import com.sdydj.common.utils.R;



/**
 * 商品属性
 *
 * @author sdydj
 * @email sdy@gmail.com
 * @date 2021-04-18 02:30:01
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

    @Autowired
    private ProductAttrValueService productAttrValueService;
    ///product/attr/base/listforspu/{spuId}
    @GetMapping("/base/listforspu/{spuId}")
    public  R baseAttrlistforSpu(@PathVariable Long spuId){
        List<ProductAttrValueEntity> entities= productAttrValueService.baseAttrlistforspu(spuId);
        return R.ok().put("data",entities);
    }
    ///product/attr/base/list/{catelogId}
    //@GetMapping("/base/list/{catelogId}")
    @GetMapping("/{attrType}/list/{catelogId}")
    public  R baseAttrList(@RequestParam Map<String, Object> params,
                           @PathVariable("catelogId") Long catelogId,
                           @PathVariable("attrType") String type){

        PageUtils page = attrService.queryBaseAttrPage(params,catelogId,type);
        return R.ok().put("page",page);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
   // @RequiresPermissions("product:attr:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    //@RequiresPermissions("product:attr:info")
    public R info(@PathVariable("attrId") Long attrId){
		//AttrEntity attr = attrService.getById(attrId);
        AttrRespVo respVo=attrService.getAttrinfo(attrId);
        return R.ok().put("attr", respVo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attr:save")
    public R save(@RequestBody AttrVo attr){
		attrService.saveAttr(attr);

        return R.ok();
    }
    ///product/attr/update/{spuId}
    @PostMapping("/update/{spuId}")
    //@RequiresPermissions("product:attr:update")
    public R updateSpuAttr(@PathVariable Long spuId,
                           @RequestBody List<ProductAttrValueEntity> entities){
        //attrService.updateById(attr);
        productAttrValueService.updateSpuAttr(spuId,entities);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attr:update")
    public R update(@RequestBody AttrVo attr){
		//attrService.updateById(attr);
		attrService.updateAttr(attr);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attr:delete")
    public R delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
