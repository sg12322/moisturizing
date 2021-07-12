package com.sdydj.moisturizing.product.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sdydj.common.utils.PageUtils;
import com.sdydj.common.utils.Query;

import com.sdydj.moisturizing.product.dao.SkuInfoDao;
import com.sdydj.moisturizing.product.entity.SkuInfoEntity;
import com.sdydj.moisturizing.product.service.SkuInfoService;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                new QueryWrapper<SkuInfoEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveSkuInfo(SkuInfoEntity skuInfoEntity) {
        this.baseMapper.insert(skuInfoEntity);
    }

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        /*
        key: '华为',//检索关键字
        catelogId: 0,
        brandId: 0,
        min: 0,
        max: 0
         */
        QueryWrapper<SkuInfoEntity> wrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        if(!StringUtils.isEmpty(key)){
            wrapper.and((a)->{
                a.eq("sku_id",key).or().like("sku_name",key);
           });
        }
        String catelogId = (String) params.get("catelogId");
        if(!StringUtils.isEmpty(catelogId) &&  !"0".equalsIgnoreCase(catelogId)){

                wrapper.eq("catalog_id",catelogId);
        }
        String brandId = (String) params.get("brandId");
        if(!StringUtils.isEmpty(brandId)&&  !"0".equalsIgnoreCase(brandId)){
            wrapper.eq("brand_id",brandId);
        }
        String min = (String) params.get("min");
        String max = (String) params.get("max");
        if(!StringUtils.isEmpty(min)){
            wrapper.ge("price",min);
        }
        if(!StringUtils.isEmpty(max)){
            try {
                BigDecimal bigDecimal = new BigDecimal(max);
                if(bigDecimal.compareTo(new BigDecimal("0"))==1){
                    wrapper.le("price",max);
                }

            }catch(Exception e){

            }
         ;
        }

        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    @Override
    public List<SkuInfoEntity> getSkuBySpuId(Long spuId) {
        List<SkuInfoEntity> entities = this.list(new QueryWrapper<SkuInfoEntity>().eq("spu_id", spuId));
        return entities;
    }
    /*
    eq 就是 equal等于
    ne就是 not equal不等于
    gt 就是 greater than大于
    lt 就是 less than小于
    ge 就是 greater than or equal 大于等于
    le 就是 less than or equal 小于等于
    in 就是 in 包含（数组）
    isNull 就是 等于null
    between 就是 在2个条件之间(包括边界值)
    like就是 模糊查询

     */

}