package com.sdydj.moisturizing.ware.service.impl;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sdydj.common.utils.PageUtils;
import com.sdydj.common.utils.Query;

import com.sdydj.moisturizing.ware.dao.WareInfoDao;
import com.sdydj.moisturizing.ware.entity.WareInfoEntity;
import com.sdydj.moisturizing.ware.service.WareInfoService;


@Service("wareInfoService")
public class WareInfoServiceImpl extends ServiceImpl<WareInfoDao, WareInfoEntity> implements WareInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        String key = (String) params.get("key");
        QueryWrapper<WareInfoEntity> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(key)){
                wrapper.eq("id",key).or().like("name",key)
                        .or().like("address",key)
                        .or().like("areacode",key);
        }
        IPage<WareInfoEntity> page = this.page(
                new Query<WareInfoEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

}