package com.shop.product.domain.sku;

import com.shop.product.domain.event.DomainEvent;
import com.shop.product.domain.shared.AggregateRoot;
import com.shop.product.domain.valueobject.Money;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * SKU聚合根
 * SKU（库存量单位）是商品的具体规格组合，是库存管理的最小单元
 * 
 * 业务规则：
 * 1. 同一商品下SKU属性组合不能重复
 * 2. SKU编码必须唯一
 * 3. SKU数量不能超过限制（默认100）
 * 4. SKU必须关联有效商品
 * 
 * @author DDD架构师
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Sku extends AggregateRoot<Long> {
    
    /**
     * 所属商品ID
     */
    private Long productId;
    
    /**
     * SKU编码（唯一）
     */
    private String skuCode;
    
    /**
     * 条形码
     */
    private String barcode;
    
    /**
     * 属性组合
     */
    private List<SkuAttribute> attributes = new ArrayList<>();
    
    /**
     * 售价
     */
    private Money price;
    
    /**
     * 成本价
     */
    private Money costPrice;
    
    /**
     * SKU状态
     */
    private SkuStatus status;
    
    /**
     * 创建SKU
     * 
     * @param productId 商品ID
     * @param skuCode SKU编码
     * @param barcode 条形码
     * @param attributes 属性组合
     * @param price 售价
     * @param costPrice 成本价
     * @return 新创建的SKU
     */
    public static Sku create(Long productId, String skuCode, String barcode,
                              List<SkuAttribute> attributes, Money price, Money costPrice) {
        Sku sku = new Sku();
        sku.setProductId(productId);
        sku.setSkuCode(skuCode);
        sku.setBarcode(barcode);
        sku.setAttributes(new ArrayList<>(attributes));
        sku.setPrice(price);
        sku.setCostPrice(costPrice);
        sku.setStatus(SkuStatus.ENABLED);
        sku.preCreate();
        
        // 发布SKU创建事件
        sku.registerEvent(new SkuCreatedEvent(sku.getId(), productId));
        
        return sku;
    }
    
    /**
     * 更新SKU信息
     */
    public void update(String barcode, Money price, Money costPrice) {
        if (status == SkuStatus.DISABLED) {
            throw new IllegalStateException("Cannot update disabled SKU");
        }
        
        this.barcode = barcode;
        this.price = price;
        this.costPrice = costPrice;
        this.preUpdate();
        
        registerEvent(new SkuUpdatedEvent(this.id, this.productId));
    }
    
    /**
     * 禁用SKU
     */
    public void disable() {
        if (this.status == SkuStatus.DISABLED) {
            return;
        }
        
        this.status = SkuStatus.DISABLED;
        this.preUpdate();
        
        registerEvent(new SkuStatusChangedEvent(this.id, this.productId, SkuStatus.DISABLED));
    }
    
    /**
     * 启用SKU
     */
    public void enable() {
        if (this.status == SkuStatus.ENABLED) {
            return;
        }
        
        this.status = SkuStatus.ENABLED;
        this.preUpdate();
        
        registerEvent(new SkuStatusChangedEvent(this.id, this.productId, SkuStatus.ENABLED));
    }
    
    /**
     * 获取属性签名（用于判断属性组合是否重复）
     */
    public String getAttributeSignature() {
        if (attributes == null || attributes.isEmpty()) {
            return "";
        }
        
        return attributes.stream()
                .sorted((a, b) -> a.getAttributeId().compareTo(b.getAttributeId()))
                .map(attr -> attr.getAttributeId() + ":" + attr.getAttributeValue())
                .collect(Collectors.joining("|"));
    }
    
    /**
     * 判断是否启用
     */
    public boolean isEnabled() {
        return status == SkuStatus.ENABLED;
    }
    
    /**
     * 判断是否禁用
     */
    public boolean isDisabled() {
        return status == SkuStatus.DISABLED;
    }
}

/**
 * SKU状态枚举
 */
enum SkuStatus {
    ENABLED(1, "启用"),
    DISABLED(0, "禁用");
    
    private final int code;
    private final String description;
    
    SkuStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }
    
    public int getCode() {
        return code;
    }
    
    public String getDescription() {
        return description;
    }
}

/**
 * SKU创建事件
 */
class SkuCreatedEvent extends DomainEvent {
    
    private final Long productId;
    
    public SkuCreatedEvent(Long skuId, Long productId) {
        super(String.valueOf(skuId), "Sku");
        this.productId = productId;
    }
    
    public Long getProductId() {
        return productId;
    }
}

/**
 * SKU更新事件
 */
class SkuUpdatedEvent extends DomainEvent {
    
    private final Long productId;
    
    public SkuUpdatedEvent(Long skuId, Long productId) {
        super(String.valueOf(skuId), "Sku");
        this.productId = productId;
    }
    
    public Long getProductId() {
        return productId;
    }
}

/**
 * SKU状态变更事件
 */
class SkuStatusChangedEvent extends DomainEvent {
    
    private final Long productId;
    private final SkuStatus newStatus;
    
    public SkuStatusChangedEvent(Long skuId, Long productId, SkuStatus newStatus) {
        super(String.valueOf(skuId), "Sku");
        this.productId = productId;
        this.newStatus = newStatus;
    }
    
    public Long getProductId() {
        return productId;
    }
    
    public SkuStatus getNewStatus() {
        return newStatus;
    }
}
