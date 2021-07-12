package com.sdydj.moisturizing.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.sdydj.common.to.es.SkuEsModel;
import com.sdydj.moisturizing.search.config.MoisturizingElasticSearchConfig;
import com.sdydj.moisturizing.search.constant.EsConstant;
import com.sdydj.moisturizing.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service("ProductSaveService")
public class ProductSaveServiceImpl implements ProductSaveService {

    @Autowired
    RestHighLevelClient restHighLevelClient;

    @Override
    public boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException {
        //保存到es
        //给es中建立索引 product 建立好映射关系


        //给es中批量保存所有的数据
        //BulkRequest bulkRequest, RequestOptions options
        BulkRequest bulkRequest = new BulkRequest();
        for (SkuEsModel skuEsModel : skuEsModels) {
            //构造保存请求
            IndexRequest indexRequest = new IndexRequest(EsConstant.PRODUCUT_INDEX);
            indexRequest.id(skuEsModel.getSkuId().toString());
            String jsonString = JSON.toJSONString(skuEsModel);
            indexRequest.source(jsonString, XContentType.JSON);
            bulkRequest.add(indexRequest);
        }
        BulkResponse bulk = restHighLevelClient.bulk(bulkRequest, MoisturizingElasticSearchConfig.COMMON_OPTIONS);
        //TODO 如果批量错误
        boolean failures = bulk.hasFailures();
        List<String> collect = Arrays.stream(bulk.getItems()).map((item) -> {
            return item.getId();
        }).collect(Collectors.toList());
        log.error("商品上架完成：{},返回数据：{}",collect,bulk.toString());
        return  failures;
    }
}
