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
 * 创建SKU请求
 * 
 * @author DDD架构师
 * @since 1.0.0
 */
@Data
@Schema(description = "创建SKU请求")
public class CreateSkuRequest {
    
    @Schema(description = "SKU编码")
    @NotBlank(message = "SKU编码不能为空")
    private String skuCode;
    
    @Schema(description = "条形码")
    private String barcode;
    
    @Schema(description = "属性组合")
    @Valid
    @NotNull(message = "属性组合不能为空")
    private List<SkuAttributeRequest> attributes;
    
    @Schema(description = "售价")
    @NotNull(message = "售价不能为空")
    @Positive(message = "售价必须大于0")
    private BigDecimal price;
    
    @Schema(description = "成本价")
    private BigDecimal costPrice;
    
    @Schema(description = "初始库存")
    @NotNull(message = "初始库存不能为空")
    @Positive(message = "初始库存必须大于等于0")
    private BigDecimal initialStock;
    
    @Schema(description = "仓库ID")
    @NotNull(message = "仓库ID不能为空")
    private Long warehouseId;
}
