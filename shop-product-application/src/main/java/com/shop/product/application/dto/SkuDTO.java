package com.shop.product.application.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * SKU DTO
 * 
 * @author DDD架构师
 * @since 1.0.0
 */
@Data
public class SkuDTO {
    
    /**
     * SKU ID
     */
    private Long id;
    
    /**
     * 所属商品ID
     */
    private Long productId;
    
    /**
     * SKU编码
     */
    private String skuCode;
    
    /**
     * 条形码
     */
    private String barcode;
    
    /**
     * 属性组合
     */
    private List<SkuAttributeDTO> attributes;
    
    /**
     * 属性描述（用于展示）
     */
    private String attributeDesc;
    
    /**
     * 售价
     */
    private BigDecimal price;
    
    /**
     * 成本价
     */
    private BigDecimal costPrice;
    
    /**
     * SKU状态
     */
    private Integer status;
    
    /**
     * 状态描述
     */
    private String statusDesc;
    
    /**
     * 库存信息
     */
    private List<InventoryDTO> inventories;
    
    /**
     * 总库存
     */
    private BigDecimal totalStock;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
