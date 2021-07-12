package com.sdydj.moisturizing.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sdydj.common.utils.PageUtils;
import com.sdydj.moisturizing.product.entity.BrandEntity;

import java.util.Map;

/**
 * 品牌
 *
 * @author sdy
 * @email sdy@gmail.com
 * @date 2021-04-17 02:14:59
 */
public interface BrandService extends IService<BrandEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 更新关联数据
     * @param brand
     */
    void updateDetail(BrandEntity brand);

}

