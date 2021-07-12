package com.sdydj.common.to;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SpuBounbdTo {
    private Long spuID;
    private BigDecimal buyBounds;
    private BigDecimal growBounds;

}
