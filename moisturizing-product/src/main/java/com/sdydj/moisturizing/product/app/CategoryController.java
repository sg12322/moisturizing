package com.sdydj.moisturizing.product.app;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sdydj.moisturizing.product.entity.CategoryEntity;
import com.sdydj.moisturizing.product.service.CategoryService;
import com.sdydj.common.utils.R;



/**
 * 商品三级分类
 *
 * @author sdydj
 * @email sdy@gmail.com
 * @date 2021-04-18 02:30:01
 */
@RestController
@RequestMapping("product/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 树形结构查查出所有
     */
    @RequestMapping("/list/tree")
   // @RequiresPermissions("product:category:list")
    public R list(){

        List<CategoryEntity> categoryEntity=categoryService.listWithTree();

        return R.ok().put("data",categoryEntity);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{catId}")
    //@RequiresPermissions("product:category:info")
    public R info(@PathVariable("catId") Long catId){
		CategoryEntity category = categoryService.getById(catId);

        return R.ok().put("data", category);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:category:save")
    public R save(@RequestBody CategoryEntity category){
		categoryService.save(category);

        return R.ok();
    }

    @RequestMapping("/update/sort")
    //@RequiresPermissions("product:category:update")
    public R updateSort(@RequestBody CategoryEntity[] category){
        categoryService.updateBatchById(Arrays.asList(category));

        return R.ok();
    }


    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:category:update")
    public R update(@RequestBody CategoryEntity category){
		//categoryService.updateById(category);
        categoryService.updateDetail(category);
        return R.ok();
    }

    /**
     * 删除
     * @RequestBody 获取post 请求体
     * springmvc 自动将请求体数据转换为对应对象
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:category:delete")
    public R delete(@RequestBody Long[] catIds){
        //检查当前菜单是否被引用
		//categoryService.removeByIds(Arrays.asList(catIds));
        categoryService.removeMenuByIds(Arrays.asList(catIds));

        return R.ok();
    }

}
