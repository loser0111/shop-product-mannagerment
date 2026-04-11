package com.shop.product.domain.product;

import com.shop.product.domain.shared.ValueObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * 商品图片值对象
 * 描述商品的图片信息
 * 
 * @author DDD架构师
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductImage implements ValueObject {
    
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
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductImage that = (ProductImage) o;
        return Objects.equals(url, that.url);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(url);
    }
}
