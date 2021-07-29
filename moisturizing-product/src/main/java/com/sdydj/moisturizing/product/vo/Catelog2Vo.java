package com.sdydj.moisturizing.product.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Catelog2Vo  {
    private String catalog1Id;//1级夫分类id
    private List<Catelog3Vo> catalog3List;//三级子分类
    private String id;
    private String name;

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Catelog3Vo{
        private  String catalog2Id;//2级父分类id
        private  String id;
        private  String name;

    }
}
