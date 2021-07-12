package com.sdydj.moisturizing.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sdydj.common.utils.PageUtils;
import com.sdydj.moisturizing.product.entity.AttrEntity;
import com.sdydj.moisturizing.product.vo.AttrGroupRelationVo;
import com.sdydj.moisturizing.product.vo.AttrRespVo;
import com.sdydj.moisturizing.product.vo.AttrVo;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author sdy
 * @email sdy@gmail.com
 * @date 2021-04-17 02:14:59
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveAttr(AttrVo attr);

    PageUtils queryBaseAttrPage(Map<String, Object> params, Long catelogId, String type);

    AttrRespVo getAttrinfo(Long attrId);

    void updateAttr(AttrVo attr);

    List<AttrEntity> getRelationAttr(Long attrgroupId);

    void deleteRelation(AttrGroupRelationVo[] vos);

    PageUtils getNoRelationAttr(Map<String, Object> params, Long attrgroupId);

    List<Long> selectSearchAttrIds(List<Long> attrIds);

}

