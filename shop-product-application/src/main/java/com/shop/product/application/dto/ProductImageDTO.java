package com.shop.product.application.dto;

import lombok.Data;

/**
 * 商品图片DTO
 * 
 * @author DDD架构师
 * @since 1.0.0
 */
@Data
public class ProductImageDTO {
    
    /**
     * 图片URL
     */
    private String url;
    
    /**
     * 排序号
     */
    private Integer sortOrder;
    
    /**
     * 是否主图
     */
    private Boolean isMain;
}
