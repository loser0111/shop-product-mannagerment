package com.shop.product.application.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * SKU属性命令
 * 
 * @author DDD架构师
 * @since 1.0.0
 */
@Data
public class SkuAttributeCommand {
    
    /**
     * 属性ID
     */
    @NotNull(message = "属性ID不能为空")
    private Long attributeId;
    
    /**
     * 属性名称
     */
    @NotBlank(message = "属性名称不能为空")
    private String attributeName;
    
    /**
     * 属性值
     */
    @NotBlank(message = "属性值不能为空")
    private String attributeValue;
    
    /**
     * 排序号
     */
    private Integer sortOrder;
}
