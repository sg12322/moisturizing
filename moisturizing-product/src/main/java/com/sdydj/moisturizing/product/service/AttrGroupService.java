package com.sdydj.moisturizing.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sdydj.common.utils.PageUtils;
import com.sdydj.moisturizing.product.entity.AttrGroupEntity;
import com.sdydj.moisturizing.product.vo.AtrrGroupWithAttrVo;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author sdy
 * @email sdy@gmail.com
 * @date 2021-04-17 02:14:58
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);



    PageUtils queryPage(Map<String, Object> params, Long catelogId);

    List<AtrrGroupWithAttrVo> getAttrGroupWithAttrsByCatelogId(Long catelogId);

}

