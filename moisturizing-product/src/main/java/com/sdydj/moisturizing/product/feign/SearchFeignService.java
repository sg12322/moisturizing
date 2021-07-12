package com.sdydj.moisturizing.product.feign;

import com.sdydj.common.to.es.SkuEsModel;
import com.sdydj.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("moisturizing-search")
public interface SearchFeignService {
    @PostMapping("search/save/product")
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels);
}
