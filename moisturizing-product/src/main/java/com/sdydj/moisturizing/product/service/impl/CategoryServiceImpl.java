package com.sdydj.moisturizing.product.service.impl;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sdydj.common.utils.PageUtils;
import com.sdydj.common.utils.Query;

import com.sdydj.moisturizing.product.dao.CategoryDao;
import com.sdydj.moisturizing.product.entity.CategoryEntity;
import com.sdydj.moisturizing.product.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    /***
     * 得到一级菜单
     * @return
     */
    @Override
    public List<CategoryEntity> listWithTree() {
        List<CategoryEntity> categoryEntities = baseMapper.selectList(null);
        List<CategoryEntity> collect = categoryEntities.stream().filter(entity->
             entity.getParentCid() == 0
        ).map(entity->{
                 entity.setChildren(getChildren(entity,categoryEntities));
            return entity;
        }).sorted((m1,m2)->{
            return  m1.getSort()-m2.getSort();
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public void removeMenuByIds(List<Long> asList) {
        //TODO 检查当前删除的菜单是否被引用
        baseMapper.deleteBatchIds(asList);
    }

    /***
     * 得到子菜单
     * @param root
     * @param categoryEntities
     * @return
     */
    public List<CategoryEntity> getChildren(CategoryEntity root,List<CategoryEntity>categoryEntities){
        List<CategoryEntity> categoryEntityStream = categoryEntities.stream().filter(entity ->
                entity.getParentCid() == root.getCatId()
        ).map((entity) -> {
            entity.setChildren(getChildren(entity, categoryEntities));
            return entity;
        }).sorted((m1,m2)->{
            return (m1.getSort()==null?0:m1.getSort())-(m2.getSort()==null?0:m2.getSort());
        }).collect(Collectors.toList());;
        return categoryEntityStream;
    }

}