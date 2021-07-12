package com.sdydj.moisturizing.product.service.impl;

import com.sdydj.moisturizing.product.entity.AttrEntity;
import com.sdydj.moisturizing.product.service.AttrService;
import com.sdydj.moisturizing.product.vo.AtrrGroupWithAttrVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sdydj.common.utils.PageUtils;
import com.sdydj.common.utils.Query;

import com.sdydj.moisturizing.product.dao.AttrGroupDao;
import com.sdydj.moisturizing.product.entity.AttrGroupEntity;
import com.sdydj.moisturizing.product.service.AttrGroupService;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {
    @Autowired
    AttrService attrService;
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catelogId) {
        String key = (String) params.get("key");
        QueryWrapper<AttrGroupEntity> Wrapper = new QueryWrapper<AttrGroupEntity>();
        if (!StringUtils.isEmpty(key)) {
            Wrapper.and((obj) -> {
                obj.eq("attr_group_id", key).or().like("attr_group_name", key);
            });

        }
        if (catelogId == 0) {
            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params), Wrapper);
            return new PageUtils(page);
        } else {
            //select * from attr_group where catelog_id =catelogId and (attr_group_id =key or attr_group_name like %key%)
            Wrapper.eq("catelog_id", catelogId);

            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params), Wrapper);
            PageUtils pageUtils = new PageUtils(page);
            return pageUtils;
        }
    }

    /**
     * 根据分类id 查出分组信息 以及 分组下的属性
     * @param catelogId
     * @return
     */
    @Override
    public List<AtrrGroupWithAttrVo> getAttrGroupWithAttrsByCatelogId(Long catelogId) {
        //分组信息
        List<AttrGroupEntity> attrGroupEntities = this.list(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catelogId));
        //查询所有属性
        List<AtrrGroupWithAttrVo> collect = attrGroupEntities.stream().map(group -> {

            AtrrGroupWithAttrVo attrVos = new AtrrGroupWithAttrVo();
            BeanUtils.copyProperties(group, attrVos);
            List<AttrEntity> attrs = attrService.getRelationAttr(attrVos.getAttrGroupId());
            attrVos.setAttrs(attrs);
            return attrVos;
        }).collect(Collectors.toList());


        return collect;
    }

}