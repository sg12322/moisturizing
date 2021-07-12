package com.sdydj.moisturizing.search.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sdydj.common.to.es.SkuEsModel;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;


public interface ProductSaveService{
    boolean productStatusUp(List<SkuEsModel> skuEsModels) throws IOException;

}
