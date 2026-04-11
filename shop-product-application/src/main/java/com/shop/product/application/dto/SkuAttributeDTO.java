package com.shop.product.application.dto;

import lombok.Data;

/**
 * SKU属性DTO
 * 
 * @author DDD架构师
 * @since 1.0.0
 */
@Data
public class SkuAttributeDTO {
    
    /**
     * 属性ID
     */
    private Long attributeId;
    
    /**
     * 属性名称
     */
    private String attributeName;
    
    /**
     * 属性值
     */
    private String attributeValue;
    
    /**
     * 排序号
     */
    private Integer sortOrder;
}
