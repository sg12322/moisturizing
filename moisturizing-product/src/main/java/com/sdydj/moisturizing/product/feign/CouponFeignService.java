package com.sdydj.moisturizing.product.feign;

import com.sdydj.common.to.SkuReductionTo;
import com.sdydj.common.to.SpuBounbdTo;
import com.sdydj.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("moisturizing-coupon")
public interface CouponFeignService {

    /**
     * CouponFeignService.saveSpuBounds(SpuBounbdTo spuBounbdTo)
     *                  @RequestBody将这个对象转为json
     *                  去注册中心找到moisturizing-coupon服务给coupon/spubounds/save发送请求。将上一步的JSON放在请求体的位置发送请求
     *                   对方服务收到请求 请求体里有JSON数据
     *                   @RequestBody SpuBoundsEntity spuBounds 将请求体的 JSON 转为  SpuBoundsEntity
     *  只要JSON数据模型是兼容的 双方服务无需使用一个to
     * @param spuBounbdTo
     * @return
     */
    @PostMapping("coupon/spubounds/save")
    R saveSpuBounds(@RequestBody SpuBounbdTo spuBounbdTo);
    @PostMapping("coupon/skufullreduction/saveinfo")
    R savebSkuRedection(@RequestBody SkuReductionTo skuReductionTo);

}
