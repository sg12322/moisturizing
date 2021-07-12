package com.sdydj.moisturizing.ware.feign;

import com.sdydj.common.to.SpuBounbdTo;
import com.sdydj.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient("moisturizing-product")
public interface ProductFeignService {

    /**
     * api/product/skuinfo/info/{skuId}  过网关 moisturizing-getay
     *roduct/skuinfo/info/{skuId}  直接调用后台 moisturizing-getayw
     *
     *
     * @param skuId
     * @return
     */
    @GetMapping(" product/skuinfo/info/{skuId}")
    R getSkuInfo(@PathVariable Long skuId);
}
