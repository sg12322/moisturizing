package com.sdydj.moisturizing.product.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.sdydj.common.constant.Productconstant;
import com.sdydj.common.to.SkuReductionTo;
import com.sdydj.common.to.SpuBounbdTo;
import com.sdydj.common.to.es.SkuEsModel;
import com.sdydj.common.utils.R;
import com.sdydj.moisturizing.product.entity.*;
import com.sdydj.moisturizing.product.feign.CouponFeignService;
import com.sdydj.moisturizing.product.feign.SearchFeignService;
import com.sdydj.moisturizing.product.feign.WareFeignService;
import com.sdydj.moisturizing.product.service.*;
import com.sdydj.moisturizing.product.vo.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sdydj.common.utils.PageUtils;
import com.sdydj.common.utils.Query;

import com.sdydj.moisturizing.product.dao.SpuInfoDao;
import org.springframework.transaction.annotation.Transactional;


@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {


    @Autowired
    SpuInfoDescService spuInfoDescService;

    @Autowired
    SpuImagesService spuImagesService;

    @Autowired
    AttrService attrService;


    @Autowired
    ProductAttrValueService productAttrValueService;


    @Autowired
    SkuInfoService skuInfoService;


    @Autowired
    SkuImagesService skuImagesService;


    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;


    @Autowired
    CouponFeignService couponFeignService;

    @Autowired
    BrandService brandService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    WareFeignService wareFeignService;

    @Autowired
    SearchFeignService searchFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<SpuInfoEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * TODO 高级部分
     *
     * @param vo
     */
    @Transactional
    @Override
    public void saveSpuInfo(SpuSaveVo vo) {
        //保存spu基本信息  pms_spu_info

        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(vo, spuInfoEntity);
        spuInfoEntity.setCreateTime(new Date());
        spuInfoEntity.setUpdateTime(new Date());
        this.saveBaseSpuInfo(spuInfoEntity);

        //保存spu的描述图片    pms_spu_info_desc
        List<String> decript = vo.getDecript();
        SpuInfoDescEntity descEntity = new SpuInfoDescEntity();
        descEntity.setSpuId(spuInfoEntity.getId());
        descEntity.setDecript(String.join(",", decript));
        spuInfoDescService.saveSpuInfoDesc(descEntity);

        //保存spu的图片集    pms_spu_images
        List<String> images = vo.getImages();
        spuImagesService.saveImages(spuInfoEntity.getId(), images);

        //保存spu的规格参数   pms_product_attr_value

        List<BaseAttrs> baseAttrs = vo.getBaseAttrs();

        List<ProductAttrValueEntity> collect = baseAttrs.stream().map((item) -> {
            ProductAttrValueEntity attrValueEntity = new ProductAttrValueEntity();
            attrValueEntity.setAttrId(item.getAttrId());
            AttrEntity byId = attrService.getById(item.getAttrId());
            attrValueEntity.setAttrName(byId.getAttrName());
            attrValueEntity.setAttrValue(item.getAttrValues());
            attrValueEntity.setQuickShow(item.getShowDesc());
            attrValueEntity.setSpuId(spuInfoEntity.getId());
            return attrValueEntity;

        }).collect(Collectors.toList());
        productAttrValueService.saveProductAttr(collect);

        //保存spu的积分信息    sms   sms_spu_bounds
        Bounds bounds = vo.getBounds();
        SpuBounbdTo spuBounbdTo = new SpuBounbdTo();
        BeanUtils.copyProperties(bounds, spuBounbdTo);
        spuBounbdTo.setSpuID(spuInfoEntity.getId());

        R r = couponFeignService.saveSpuBounds(spuBounbdTo);
        if (r.getCode() != 0) {
            log.error("远程保存spu积分信息失败");
        }
        ;


        //保存spu的所有sku信息

              /*
              *    private String skuName;
                      private BigDecimal price;
               private String skuTitle;
               private String skuSubtitle;
               *
               */
        List<Skus> skus = vo.getSkus();
        if (skus != null && skus.size() > 0) {
            skus.forEach((item) -> {
                String defaltImg = "";
                for (Images image : item.getImages()) {
                    if (image.getDefaultImg() == 1) {
                        defaltImg = image.getImgUrl();
                    }
                }


                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                BeanUtils.copyProperties(item, skuInfoEntity);
                skuInfoEntity.setBrandId(spuInfoEntity.getBrandId());
                skuInfoEntity.setCatalogId(spuInfoEntity.getCatalogId());
                skuInfoEntity.setSaleCount(0L);
                skuInfoEntity.setSpuId(spuInfoEntity.getId());
                skuInfoEntity.setSkuDefaultImg(defaltImg);
                //sku的基本信息  pms_sku_info
                skuInfoService.saveSkuInfo(skuInfoEntity);


                Long skuId = skuInfoEntity.getSkuId();
                List<Images> itemImages = item.getImages();

                List<SkuImagesEntity> skuImageList = itemImages.stream().map((img) -> {
                    SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                    skuImagesEntity.setSkuId(skuId);
                    skuImagesEntity.setImgUrl(img.getImgUrl());
                    skuImagesEntity.setDefaultImg(img.getDefaultImg());
                    return skuImagesEntity;
                }).filter((entityes) -> {
                    //返回true就是需要 false 就是剔除
                    return !StringUtils.isEmpty(entityes.getImgUrl());
                }).collect(Collectors.toList());
                //sku的图片信息   pms_sku_images
                skuImagesService.saveBatch(skuImageList);
//                         /TODO 没有图片路径不保存
                //sku的销售属性  pms_sku_sale_attr_value
                List<Attr> attrs = item.getAttr();
                List<SkuSaleAttrValueEntity> skuSaleAttrList = attrs.stream().map((attr) -> {
                    SkuSaleAttrValueEntity skuSaleAttrValueEntity = new SkuSaleAttrValueEntity();
                    BeanUtils.copyProperties(attr, skuSaleAttrValueEntity);
                    skuSaleAttrValueEntity.setSkuId(skuId);
                    return skuSaleAttrValueEntity;
                }).collect(Collectors.toList());

                skuSaleAttrValueService.saveBatch(skuSaleAttrList);


                //sku的优惠满减信息  sms     sms_sku_ladder   sms_sku_full_reduction   sms_member_price  sms_spu_bounds
                SkuReductionTo skuReductionTo = new SkuReductionTo();
                BeanUtils.copyProperties(item, skuReductionTo);
                skuReductionTo.setSkuId(skuId);
                if (skuReductionTo.getFullCount() > 0 || skuReductionTo.getFullPrice().compareTo(new BigDecimal("0")) == 1) {
                    R r1 = couponFeignService.savebSkuRedection(skuReductionTo);
                    if (r1.getCode() != 0) {
                        log.error("远程调用优惠信息失败");
                    }
                }

            });

        }


    }

    @Override
    public void saveBaseSpuInfo(SpuInfoEntity spuInfoEntity) {
        this.baseMapper.insert(spuInfoEntity);
    }

    /*
       key:
       brandId: 5
       catelogId: 225
       status: 0
     */
    @Override
    public PageUtils queryPageByConditon(Map<String, Object> params) {
        QueryWrapper<SpuInfoEntity> wrapper = new QueryWrapper<>();
        String key = (String) params.get("key");
        if (!StringUtils.isEmpty(key)) {
            wrapper.and((w) -> {
                w.eq("id", key).or().like("spu_name", key);
            });
        }
        String brandId = (String) params.get("brandId");
        if (!StringUtils.isEmpty(brandId) && !"0".equalsIgnoreCase(brandId)) {
            wrapper.eq("brand_id", brandId);
        }
        String catelogId = (String) params.get("catelogId");
        if (!StringUtils.isEmpty(catelogId) && !"0".equalsIgnoreCase(catelogId)) {
            wrapper.eq("catalog_id", catelogId);
        }
        String status = (String) params.get("status");
        if (!StringUtils.isEmpty(status)) {
            wrapper.eq("publish_status", status);
        }

        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                wrapper
        );
        PageUtils pageUtils = new PageUtils(page);
        return pageUtils;
    }

    /**
     * 商品上架
     *
     * @param spuId
     */
    @Override
    public void up(Long spuId) {

        //查出sku信息
        List<SkuInfoEntity> entities = skuInfoService.getSkuBySpuId(spuId);
        List<Long> skuIds = entities.stream().map(SkuInfoEntity::getSkuId).collect(Collectors.toList());

        //查询当前sku的所有可检索的规格属性
        List<ProductAttrValueEntity> productAttrValueEntities = productAttrValueService.baseAttrlistforspu(spuId);
        List<Long> attrIdS = productAttrValueEntities.stream().map((item) -> {
            return item.getAttrId();
        }).collect(Collectors.toList());
        List<Long> searchAttrIds = attrService.selectSearchAttrIds(attrIdS);
        Set<Long> idSet = new HashSet<>(searchAttrIds);
        List<SkuEsModel.Attrs> attrModel = productAttrValueEntities.stream().filter((item) -> {
            return idSet.contains(item.getAttrId());
        }).map((item) -> {
            SkuEsModel.Attrs attr = new SkuEsModel.Attrs();
            BeanUtils.copyProperties(item, attr);
            return attr;
        }).collect(Collectors.toList());

        //远程查询是否有库存
        Map<Long, Boolean> stockMap = null;
        try {
            R hasStock = wareFeignService.getSkuHasStock(skuIds);
            TypeReference<List<SkuHasStockVo>> typeReference = new TypeReference<List<SkuHasStockVo>>() {};
            stockMap = hasStock.getData(typeReference).stream().collect(Collectors.toMap(SkuHasStockVo::getSkuId, item -> item.getHasStock()));

        } catch (Exception e) {
            log.error("库存服务查询异常：原因{}", e);
         }

        Map<Long, Boolean> finalStockMap = stockMap;
        List<SkuEsModel> list = entities.stream().map((item) -> {
            //封装每个sku信息
            SkuEsModel es = new SkuEsModel();
            BeanUtils.copyProperties(item, es);
            es.setSkuImg(item.getSkuDefaultImg());
            es.setSkuPrice(item.getPrice());
            //设置库存
            if (finalStockMap == null) {
                es.setHasStock(true);
            } else {
                es.setHasStock(finalStockMap.get(item.getSkuId()));
            }


            //TODO 查询热度评分
            es.setHotScore(0L);

            //查询品牌和分类的名字信息
            BrandEntity brand = brandService.getById(item.getBrandId());
            es.setBrandName(brand.getName());
            es.setBrandImg(brand.getLogo());
            CategoryEntity category = categoryService.getById(item.getCatalogId());
             es.setCatalogName(category.getName());
            //设置检索属性
            es.setAttrs(attrModel);
            return es;
        }).collect(Collectors.toList());

        //将数据发送给es保存 moisturizing-search

        R r = searchFeignService.productStatusUp(list);
        if (r.getCode()==0){
            //远程调用成功
            // TODO 修改当前spu上架状态
            this.baseMapper.updateUpdateStau(spuId, Productconstant.UpStatusEnum.SPU_UP.getCode());
        }else {
            //远程调用失败
            //TODO 重复调用？ 接口幂等性 重试机制？
            //feign 调用流程
                /*
                 1.构造请求对象
                        RequestTemplate template = buildTemplateFromArgs.create(argv);
                 2.发送请求进行执行（执行成功会解码）
                        executeAndDecode(template, options);
                 3.执行请 求有重试机制
                        while (true) {
                        try{
                         executeAndDecode(template, options);
                         }catch(){
                                 try{ retryer.continueOrPropagate(e); }catch(){throw ex; }
                              continue;
                            }
                        }
                 */
        }
    }

}