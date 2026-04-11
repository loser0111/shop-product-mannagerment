package com.shop.product.application.command;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 创建商品命令
 * 
 * @author DDD架构师
 * @since 1.0.0
 */
@Data
public class CreateProductCommand {
    
    /**
     * 商品名称
     */
    @NotBlank(message = "商品名称不能为空")
    private String name;
    
    /**
     * 分类ID
     */
    @NotNull(message = "分类ID不能为空")
    private Long categoryId;
    
    /**
     * 品牌ID
     */
    @NotNull(message = "品牌ID不能为空")
    private Long brandId;
    
    /**
     * 供应商ID
     */
    private Long supplierId;
    
    /**
     * 主图URL
     */
    @NotBlank(message = "主图不能为空")
    private String mainImage;
    
    /**
     * 售价
     */
    @NotNull(message = "售价不能为空")
    @Positive(message = "售价必须大于0")
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
    @Valid
    private List<ProductImageCommand> images;
    
    /**
     * SKU列表
     */
    @Valid
    private List<CreateSkuCommand> skus;
}
