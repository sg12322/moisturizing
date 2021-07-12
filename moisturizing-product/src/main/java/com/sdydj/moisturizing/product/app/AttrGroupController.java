package com.sdydj.moisturizing.product.app;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.sdydj.moisturizing.product.entity.AttrEntity;
import com.sdydj.moisturizing.product.service.AttrAttrgroupRelationService;
import com.sdydj.moisturizing.product.service.AttrService;
import com.sdydj.moisturizing.product.service.CategoryService;
import com.sdydj.moisturizing.product.vo.AtrrGroupWithAttrVo;
import com.sdydj.moisturizing.product.vo.AttrGroupRelationVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.sdydj.moisturizing.product.entity.AttrGroupEntity;
import com.sdydj.moisturizing.product.service.AttrGroupService;
import com.sdydj.common.utils.PageUtils;
import com.sdydj.common.utils.R;



/**
 * 属性分组
 *
 * @author sdydj
 * @email sdy@gmail.com
 * @date 2021-04-18 02:30:01
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    AttrService attrService;

    @Autowired
    AttrAttrgroupRelationService relationService;





    ///product/attrgroup/{catelogId}/withattr
    @GetMapping("/{catelogId}/withattr")
    public R getAttrGroupWithAttrs(@PathVariable("catelogId")Long catelogId){
          //分类下的分组
          //分组下的属性

        List<AtrrGroupWithAttrVo> vos=attrGroupService.getAttrGroupWithAttrsByCatelogId(catelogId);
        return R.ok().put("data",vos);
    }
    //  /product/attrgroup/attr/relation
    @PostMapping("/attr/relation")
    public  R addRelation(@RequestBody List<AttrGroupRelationVo> vos){
    relationService.saveBatch(vos);
    return  R.ok();
    }


    /**
     * 分类关联属性
     * @param attrgroupId
     * @return
     */
    @GetMapping("/{attrgroupId}/attr/relation")
    public R attrRelation(@PathVariable Long attrgroupId){
        List<AttrEntity> entities=attrService.getRelationAttr(attrgroupId);
        return R.ok().put("data",entities);
    }

    /**
     * 分类不关联属性
     * @param params
     * @param attrgroupId
     * @return
     */
    ///product/attrgroup/{attrgroupId}/noattr/relation
    @GetMapping("/{attrgroupId}/noattr/relation")
    public R noAttrRelation(@RequestParam Map<String, Object> params,@PathVariable Long attrgroupId){
        PageUtils page=attrService.getNoRelationAttr(params,attrgroupId);
        return R.ok().put("page",page);
    }


    ///product/attrgroup/attr/relation/delete
    @PostMapping("/attr/relation/delete")
    public  R deleteRelation(@RequestBody AttrGroupRelationVo[] vos){
        attrService.deleteRelation(vos);
        return R.ok();
    }


    /**
     * 列表
     */
    @RequestMapping("/list/{catelogId}")
   // @RequiresPermissions("product:attrgroup:list")
    public R list(@RequestParam Map<String, Object> params,@PathVariable Long catelogId){
//        PageUtils page = attrGroupService.queryPage(params);
        PageUtils page =attrGroupService.queryPage(params,catelogId);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrGroupId}")
    //@RequiresPermissions("product:attrgroup:info")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        Long catelogId = attrGroup.getCatelogId();
        Long[] path=categoryService.findCatelogPath(catelogId);
        attrGroup.setCatelogPath(path);
        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("product:attrgroup:save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("product:attrgroup:update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("product:attrgroup:delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
