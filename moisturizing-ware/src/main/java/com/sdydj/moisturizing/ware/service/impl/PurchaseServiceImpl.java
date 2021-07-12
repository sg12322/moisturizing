package com.sdydj.moisturizing.ware.service.impl;

import com.sdydj.common.constant.WareConstant;
import com.sdydj.moisturizing.ware.entity.PurchaseDetailEntity;
import com.sdydj.moisturizing.ware.service.PurchaseDetailService;
import com.sdydj.moisturizing.ware.service.WareSkuService;
import com.sdydj.moisturizing.ware.vo.MergeVo;
import com.sdydj.moisturizing.ware.vo.PurchaerItemDoneVo;
import com.sdydj.moisturizing.ware.vo.PurchaseDoneVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sdydj.common.utils.PageUtils;
import com.sdydj.common.utils.Query;

import com.sdydj.moisturizing.ware.dao.PurchaseDao;
import com.sdydj.moisturizing.ware.entity.PurchaseEntity;
import com.sdydj.moisturizing.ware.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;


@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Autowired
    PurchaseDetailService purchaseDetailService;

    @Autowired
    WareSkuService wareSkuService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageUnreceive(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>().eq("status",0).or().eq("status",1)
        );

        return new PageUtils(page);
    }

    /***合并
     * @param vo
     */
    @Transactional
    @Override
    public void mergePurchase(MergeVo vo) {
        Long purchaseId = vo.getPurchaseId();
        if (purchaseId==null){
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setCreateTime(new Date());
            purchaseEntity.setUpdateTime(new Date());
            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.CREATED.getCode());
            this.save(purchaseEntity);
            purchaseId=purchaseEntity.getId();
        }
        //TODO 确认采购单状态 为0或者1
        List<Long> items = vo.getItems();
        Long finalPurchaseId = purchaseId;
        List<PurchaseDetailEntity> detailEntities = purchaseDetailService.listByIds(items);

        List<PurchaseDetailEntity> purchaseDetailEntities = purchaseDetailService.listByIds(items).stream().filter((item) -> {
            return item.getStatus() == WareConstant.PurchaseDetailStatusEnum.CREATED.getCode() ||
                    item.getStatus() == WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode()
                    ;
        }).map(i -> {
            PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
            detailEntity.setId(i.getId());
            detailEntity.setPurchaseId(finalPurchaseId);
            detailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode());
            return detailEntity;
        }).collect(Collectors.toList());
      purchaseDetailService.updateBatchById(purchaseDetailEntities);
        PurchaseEntity entity = new PurchaseEntity();
        entity.setId(purchaseId);
        entity.setUpdateTime(new Date());
        this.updateById(entity);
    }
    //领取采购单
    @Override
    public void received(List<Long> ids) {
        //确认当前采购单是新建或者已分配状态
        List<PurchaseEntity> collect = ids.stream().map(item -> {
            PurchaseEntity byId = this.getById(item);
            return byId;
        }).filter(item -> {
            if (item.getStatus() == WareConstant.PurchaseStatusEnum.CREATED.getCode() ||
                    item.getStatus() == WareConstant.PurchaseStatusEnum.ASSIGNED.getCode()) {
                return true;
            }
            return false;
        }).map((item)->{
            item.setStatus(WareConstant.PurchaseStatusEnum.RECEIVE.getCode());
            item.setUpdateTime(new Date());
            return  item;
        }).collect(Collectors.toList());
        //改变采购单状态
            this.updateBatchById(collect);
        //改变采购项的状态
       collect.forEach((item)->{
          List<PurchaseDetailEntity>  detailEntities=purchaseDetailService.ListDetailByIdPurchase(item.getId());
           List<PurchaseDetailEntity> entities = detailEntities.stream().map(enties -> {
               PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
               purchaseDetailEntity.setId(enties.getId());
               purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.BUYING.getCode());
               return purchaseDetailEntity;
           }).collect(Collectors.toList());

           purchaseDetailService.updateBatchById(entities);
       });

    }
    @Transactional
    @Override
    public void done(PurchaseDoneVo doneVo) {

         Long id = doneVo.getId();

        //改变采购项状态
        List<PurchaerItemDoneVo> items = doneVo.getItems();
        List<PurchaseDetailEntity> updates =new ArrayList<>();
        boolean flag=true;
        for (PurchaerItemDoneVo item : items) {
            PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
            if (item.getStatus()==WareConstant.PurchaseDetailStatusEnum.HASERROR.getCode()){
                flag=false;
                detailEntity.setStatus(item.getStatus());
            }else {
                detailEntity.setStatus(item.getStatus());
                //成功采购的入库
                PurchaseDetailEntity byId = purchaseDetailService.getById(item.getItemId());
                wareSkuService.addStock(byId.getSkuId(),byId.getWareId(),byId.getSkuNum());
            }

            detailEntity.setId(item.getItemId());
            updates.add(detailEntity);
        }
        purchaseDetailService.updateBatchById(updates);
        //改变采购单状态
        PurchaseEntity entity = new PurchaseEntity();
        entity.setId(id);
        entity.setStatus(flag?WareConstant.PurchaseStatusEnum.FINISH.getCode():WareConstant.PurchaseStatusEnum.HASERROR.getCode());
        entity.setUpdateTime(new Date());
        this.updateById(entity);

    }

}