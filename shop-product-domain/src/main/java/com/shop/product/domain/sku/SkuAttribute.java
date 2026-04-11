package com.shop.product.domain.sku;

import com.shop.product.domain.shared.ValueObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * SKU属性值对象
 * 描述SKU的具体属性组合
 * 
 * @author DDD架构师
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SkuAttribute implements ValueObject {
    
    /**
     * 属性ID
     */
    private Long attributeId;
    
    /**
     * 属性名称
     */
    private String attributeName;
    
    /**
     * 属性值
     */
    private String attributeValue;
    
    /**
     * 排序号
     */
    private Integer sortOrder;
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SkuAttribute that = (SkuAttribute) o;
        return Objects.equals(attributeId, that.attributeId) &&
               Objects.equals(attributeValue, that.attributeValue);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(attributeId, attributeValue);
    }
}
