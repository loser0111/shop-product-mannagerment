package com.shop.product.interfaces.http.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * 创建商品请求
 * 
 * @author DDD架构师
 * @since 1.0.0
 */
@Data
@Schema(description = "创建商品请求")
public class CreateProductRequest {
    
    @Schema(description = "商品名称")
    @NotBlank(message = "商品名称不能为空")
    private String name;
    
    @Schema(description = "分类ID")
    @NotNull(message = "分类ID不能为空")
    private Long categoryId;
    
    @Schema(description = "品牌ID")
    @NotNull(message = "品牌ID不能为空")
    private Long brandId;
    
    @Schema(description = "供应商ID")
    private Long supplierId;
    
    @Schema(description = "主图URL")
    @NotBlank(message = "主图不能为空")
    private String mainImage;
    
    @Schema(description = "售价")
    @NotNull(message = "售价不能为空")
    @Positive(message = "售价必须大于0")
    private BigDecimal price;
    
    @Schema(description = "市场价")
    private BigDecimal marketPrice;
    
    @Schema(description = "商品描述")
    private String description;
    
    @Schema(description = "商品详情HTML")
    private String detailHtml;
    
    @Schema(description = "商品图片列表")
    @Valid
    private List<ProductImageRequest> images;
    
    @Schema(description = "SKU列表")
    @Valid
    private List<CreateSkuRequest> skus;
}
