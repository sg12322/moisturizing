package com.sdydj.moisturizing.product.service.impl;

import com.sdydj.moisturizing.product.service.CategoryBrandRelationService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
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
import org.springframework.transaction.annotation.Transactional;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Autowired
    CategoryBrandRelationService categoryBrandRelationService;

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
        List<CategoryEntity> collect = categoryEntities.stream().filter(entity ->
                entity.getParentCid() == 0
        ).map(entity -> {
            entity.setChildren(getChildren(entity, categoryEntities));
            return entity;
        }).sorted((m1, m2) -> {
            return m1.getSort() - m2.getSort();
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public void removeMenuByIds(List<Long> asList) {
        //TODO 检查当前删除的菜单是否被引用
        baseMapper.deleteBatchIds(asList);
    }

    @Override
    public Long[] findCatelogPath(Long catelogId) {
        List<Long> paths = new ArrayList<>();
        List<Long> parentPath = findParentPath(catelogId, paths);
        Collections.reverse(parentPath);
        return  parentPath.toArray(new Long[parentPath.size()]);
    }
    @Transactional
    @Override
    public void updateDetail(CategoryEntity category) {
        this.updateById(category);
            categoryBrandRelationService.updataCategory(category.getCatId(),category.getName());

    }

    private List<Long> findParentPath(Long catelogId,List<Long> paths) {
        //
        paths.add(catelogId);
        CategoryEntity byId = this.getById(catelogId);

        if (byId.getParentCid() != 0) {
            findParentPath(byId.getParentCid(),paths);
        }
        return paths;
    }


    /***
     * 得到子菜单
     * @param root
     * @param categoryEntities
     * @return
     */
    public List<CategoryEntity> getChildren(CategoryEntity root, List<CategoryEntity> categoryEntities) {
        List<CategoryEntity> categoryEntityStream = categoryEntities.stream().filter(entity ->
                entity.getParentCid() == root.getCatId()
        ).map((entity) -> {
            entity.setChildren(getChildren(entity, categoryEntities));
            return entity;
        }).sorted((m1, m2) -> {
            return (m1.getSort() == null ? 0 : m1.getSort()) - (m2.getSort() == null ? 0 : m2.getSort());
        }).collect(Collectors.toList());
        ;
        return categoryEntityStream;
    }

}