package com.sdydj.moisturizing.ware.vo;

import lombok.Data;

@Data
public class PurchaerItemDoneVo {
    // items: [{itemId:1,status:4,reason:""}]//完成/失败的需求详情
    private Long itemId;
    private Integer status;
    private String reason;

}
