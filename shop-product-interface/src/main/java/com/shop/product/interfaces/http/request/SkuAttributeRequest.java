package com.shop.product.interfaces.http.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * SKU属性请求
 * 
 * @author DDD架构师
 * @since 1.0.0
 */
@Data
@Schema(description = "SKU属性")
public class SkuAttributeRequest {
    
    @Schema(description = "属性ID")
    @NotNull(message = "属性ID不能为空")
    private Long attributeId;
    
    @Schema(description = "属性名称")
    @NotBlank(message = "属性名称不能为空")
    private String attributeName;
    
    @Schema(description = "属性值")
    @NotBlank(message = "属性值不能为空")
    private String attributeValue;
    
    @Schema(description = "排序号")
    private Integer sortOrder;
}
