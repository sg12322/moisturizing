package com.sdydj.moisturizing.product.app;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sdydj.moisturizing.product.entity.BrandEntity;
import com.sdydj.moisturizing.product.vo.BrandVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sdydj.moisturizing.product.entity.CategoryBrandRelationEntity;
import com.sdydj.moisturizing.product.service.CategoryBrandRelationService;
import com.sdydj.common.utils.PageUtils;
import com.sdydj.common.utils.R;



/**
 * 品牌分类关联
 *
 * @author sdydj
 * @email sdy@gmail.com
 * @date 2021-04-18 02:30:01
 */
@RestController
@RequestMapping("product/categorybrandrelation")
public class CategoryBrandRelationController {
    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    ///product/categorybrandrelation/brands/list

    /**
     * controller 处理请求  接受校验数据
     * service 接受controller传来的苏数据 进行业务处理
     * controller 接受service处理完的数据 封装页面指定的vop
     * @param catId
     * @return
     */
    @GetMapping("/brands/list")
    public  R relationBrandList(@RequestParam(value = "catId",required = true) Long catId){
        List<BrandEntity> vos=categoryBrandRelationService.getBrands(catId);
        List<BrandVo> collect = vos.stream().map((item) -> {
            BrandVo brandVo = new BrandVo();
            brandVo.setBrandId(item.getBrandId());
            brandVo.setBrandName(item.getName());
            return brandVo;
        }).collect(Collectors.toList());
        return  R.ok().put("data",collect);
    }

    /**
     * 获取品牌关联的分类
     */
    //@RequestMapping("/brands/list")
    // @RequiresPermissions("product:categorybrandrelation:list")
    @GetMapping("/catelog/list")
    public R relationList(@RequestParam("brandId")Long brandId){
        List<CategoryBrandRelationEntity> data = categoryBrandRelationService.list(new QueryWrapper<CategoryBrandRelationEntity>().eq("brand_id", brandId));
        return R.ok().put("data", data);
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
   // @RequiresPermissions("product:categorybrandrelation:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = categoryBrandRelationService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("product:categorybrandrelation:info")
    public R info(@PathVariable("id") Long id){
		CategoryBrandRelationEntity categoryBrandRelation = categoryBrandRelationService.getById(id);

        return R.ok().put("categoryBrandRelation", categoryBrandRelation);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:categorybrandrelation:save")
    public R save(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){
		//categoryBrandRelationService.save(categoryBrandRelation);
        categoryBrandRelationService.saveDetail(categoryBrandRelation);
        return R.ok();
    }


    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:categorybrandrelation:update")
    public R update(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){
		categoryBrandRelationService.updateById(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:categorybrandrelation:delete")
    public R delete(@RequestBody Long[] ids){
		categoryBrandRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
