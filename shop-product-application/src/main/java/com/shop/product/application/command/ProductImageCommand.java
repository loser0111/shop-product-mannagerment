package com.shop.product.application.command;

import lombok.Data;

/**
 * 商品图片命令
 * 
 * @author DDD架构师
 * @since 1.0.0
 */
@Data
public class ProductImageCommand {
    
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
