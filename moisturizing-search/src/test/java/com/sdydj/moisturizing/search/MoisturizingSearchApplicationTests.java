package com.sdydj.moisturizing.search;

import com.alibaba.fastjson.JSON;
import com.sdydj.moisturizing.search.config.MoisturizingElasticSearchConfig;
import lombok.Data;
import lombok.ToString;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Avg;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.security.PrivateKey;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MoisturizingSearchApplicationTests {
    @Autowired
    private RestHighLevelClient client;


    @ToString
    @Data
    public static  class Acount {

        private int account_number;
        private int balance;
        private String firstname;
        private String lastname;
        private int age;
        private String gender;
        private String address;
        private String employer;
        private String email;
        private String city;
        private String state;


    }
    /**1方便检索
     * {
     *     skuId:1
     *     spuid:2
     *     skuName：红米
     *     price：99
     *     attrs：{
     *         cpu：gaotong，
     *         尺寸：3
     *     }
     * }
     *冗余
     *  100万*20  1000000*20kb=2000m
     *
     *
     *2
     * sku
        {
     *     skuId:1
     *     spuid:2
     *     skuName：红米
     *     price：99
     *   }
        attrs：
            {
     *    cpu：gaotong，
     *    尺寸：3
     *     }
     *  10000 商品  4000个spu  4000*8=32000byte=30kb
     *  32kb*10000=320mb；
    /**
     * 测试复杂检索
     */
    @Test
    public void searchData() throws IOException {
        //创建检索请求
        SearchRequest request = new SearchRequest();
        //设置索引
        request.indices("bank");
        //指定DSL 检索添加
        //SearchSourceBuilder
        SearchSourceBuilder builder = new SearchSourceBuilder();
         //构造检索条件
        builder.query(QueryBuilders.matchQuery("address","mill"));
        //构造聚合条件
        TermsAggregationBuilder term = AggregationBuilders.terms("ageAgg").field("age").size(10);
        builder.aggregation(term);
        AvgAggregationBuilder avgAge = AggregationBuilders.avg("ageAvg").field("age");
        builder.aggregation(avgAge);
        System.out.println("检索条件"+builder.toString());
        request.source(builder);

        //执行检索
        SearchResponse search = client.search(request, MoisturizingElasticSearchConfig.COMMON_OPTIONS);


        //分析结果 SearchResponse
        System.out.println(search.toString());
        //Map<> map = JSON.parseObject(search.toString(), Map.class);
        SearchHits hits = search.getHits();
        SearchHit[] hits1 = hits.getHits();
        /**
         * "_index" : "users",
         *         "_type" : "_doc",
         *         "_id" : "1",
         *         "_score" : 1.0,
         *         "_source" : {
         */
        for (SearchHit hit : hits1) {
           // hit.getId();
            String source = hit.getSourceAsString();
            Acount acount = JSON.parseObject(source, Acount.class);
            System.out.println(acount);
        }
        //分析聚合内容
        Aggregations aggregations = search.getAggregations();
//        for (Aggregation aggregation : aggregations) {
//            String name = aggregation.getName();
//            System.out.println(name);
//        }
        Terms ageAgg = aggregations.get("ageAgg");
        for (Terms.Bucket bucket : ageAgg.getBuckets()) {
            String string = bucket.getKeyAsString();
            long count = bucket.getDocCount();
            System.out.println(string+count);
        }
        Avg ageAvg = aggregations.get("ageAvg");
        double value = ageAvg.getValue();
        System.out.println(value);
        Aggregation avg = aggregations.get("ageAvg");

    }





    //测试保存索引

    /**
     * 测试保存数据到es
     * 更新也可以
     * @throws IOException
     */

    @Test
    public void indexData() throws IOException {
        IndexRequest request = new IndexRequest("users");//数据索引
        request.id("1");//数据id
        //request.source("name","sdydj","age","35","message","trying out Elasticsearch");
        User user = new User();
        user.setAge(21);
        user.setGender("男");
        user.setUserName("sdydj");
        String jsonString = JSON.toJSONString(user);
        request.source(jsonString, XContentType.JSON);//要保存的数据
        //执行操作
        IndexResponse index = client.index(request, MoisturizingElasticSearchConfig.COMMON_OPTIONS);
        //
        System.out.println(client);
        System.out.println(index);
    }
    @Data
    class  User{
        private  String userName;
        private String gender;
        private Integer age;
    }
    @Test
    public void contextLoads() {

        System.out.println(client);
    }

}
