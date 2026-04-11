package com.shop.product.application.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 商品DTO
 * 用于应用层和接口层之间的数据传输
 * 
 * @author DDD架构师
 * @since 1.0.0
 */
@Data
public class ProductDTO {
    
    /**
     * 商品ID
     */
    private Long id;
    
    /**
     * 商品名称
     */
    private String name;
    
    /**
     * 分类ID
     */
    private Long categoryId;
    
    /**
     * 分类名称
     */
    private String categoryName;
    
    /**
     * 品牌ID
     */
    private Long brandId;
    
    /**
     * 品牌名称
     */
    private String brandName;
    
    /**
     * 供应商ID
     */
    private Long supplierId;
    
    /**
     * 供应商名称
     */
    private String supplierName;
    
    /**
     * 商品状态
     */
    private Integer status;
    
    /**
     * 状态描述
     */
    private String statusDesc;
    
    /**
     * 主图URL
     */
    private String mainImage;
    
    /**
     * 售价
     */
    private BigDecimal price;
    
    /**
     * 市场价
     */
    private BigDecimal marketPrice;
    
    /**
     * 商品描述
     */
    private String description;
    
    /**
     * 商品详情HTML
     */
    private String detailHtml;
    
    /**
     * 商品图片列表
     */
    private List<ProductImageDTO> images;
    
    /**
     * SKU列表
     */
    private List<SkuDTO> skus;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
