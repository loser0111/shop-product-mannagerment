package com.shop.product.application.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 库存DTO
 * 
 * @author DDD架构师
 * @since 1.0.0
 */
@Data
public class InventoryDTO {
    
    /**
     * 库存ID
     */
    private Long id;
    
    /**
     * SKU ID
     */
    private Long skuId;
    
    /**
     * 仓库ID
     */
    private Long warehouseId;
    
    /**
     * 仓库名称
     */
    private String warehouseName;
    
    /**
     * 可用库存
     */
    private BigDecimal availableStock;
    
    /**
     * 锁定库存
     */
    private BigDecimal lockedStock;
    
    /**
     * 实际库存
     */
    private BigDecimal actualStock;
    
    /**
     * 预警阈值
     */
    private BigDecimal warningThreshold;
    
    /**
     * 是否需要预警
     */
    private Boolean needWarning;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
