package com.shop.product.interfaces.http.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 商品图片请求
 * 
 * @author DDD架构师
 * @since 1.0.0
 */
@Data
@Schema(description = "商品图片")
public class ProductImageRequest {
    
    @Schema(description = "图片URL")
    private String url;
    
    @Schema(description = "排序号")
    private Integer sortOrder;
    
    @Schema(description = "是否主图")
    private Boolean isMain;
}
