package com.sdydj.moisturizing.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sdydj.common.utils.PageUtils;
import com.sdydj.moisturizing.product.entity.SpuInfoDescEntity;

import java.util.Map;

/**
 * spu信息介绍
 *
 * @author sdy
 * @email sdy@gmail.com
 * @date 2021-04-17 02:14:59
 */
public interface SpuInfoDescService extends IService<SpuInfoDescEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveSpuInfoDesc(SpuInfoDescEntity descEntity);

}

