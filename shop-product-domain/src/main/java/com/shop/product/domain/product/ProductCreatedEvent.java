package com.shop.product.domain.product;

import com.shop.product.domain.event.DomainEvent;
import com.shop.product.domain.valueobject.ProductStatus;
import lombok.Getter;

/**
 * 商品创建事件
 * 
 * @author DDD架构师
 * @since 1.0.0
 */
@Getter
public class ProductCreatedEvent extends DomainEvent {
    
    public ProductCreatedEvent(Long productId) {
        super(String.valueOf(productId), "Product");
    }
}

/**
 * 商品更新事件
 */
@Getter
class ProductUpdatedEvent extends DomainEvent {
    
    public ProductUpdatedEvent(Long productId) {
        super(String.valueOf(productId), "Product");
    }
}

/**
 * 商品状态变更事件
 */
@Getter
class ProductStatusChangedEvent extends DomainEvent {
    
    private final ProductStatus newStatus;
    
    public ProductStatusChangedEvent(Long productId, ProductStatus newStatus) {
        super(String.valueOf(productId), "Product");
        this.newStatus = newStatus;
    }
}

/**
 * 商品删除事件
 */
@Getter
class ProductDeletedEvent extends DomainEvent {
    
    public ProductDeletedEvent(Long productId) {
        super(String.valueOf(productId), "Product");
    }
}
