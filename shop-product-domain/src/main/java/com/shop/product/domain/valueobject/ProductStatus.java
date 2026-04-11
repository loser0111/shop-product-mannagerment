package com.shop.product.domain.valueobject;

import com.shop.product.domain.shared.ValueObject;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * 商品状态枚举值对象
 * 定义商品的生命周期状态
 * 
 * @author DDD架构师
 * @since 1.0.0
 */
@Getter
public enum ProductStatus implements ValueObject {
    
    /**
     * 草稿状态 - 商品创建后未发布
     */
    DRAFT(0, "草稿"),
    
    /**
     * 上架状态 - 商品已发布，可在前台展示
     */
    ON_SHELF(1, "上架"),
    
    /**
     * 下架状态 - 商品已下架，前台不可见
     */
    OFF_SHELF(2, "下架"),
    
    /**
     * 删除状态 - 商品已删除（逻辑删除）
     */
    DELETED(9, "已删除");
    
    private final int code;
    private final String description;
    
    ProductStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }
    
    /**
     * 根据编码获取状态
     */
    public static ProductStatus of(int code) {
        return Arrays.stream(values())
                .filter(status -> status.code == code)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid product status code: " + code));
    }
    
    /**
     * 判断是否可以上架
     */
    public boolean canOnShelf() {
        return this == DRAFT || this == OFF_SHELF;
    }
    
    /**
     * 判断是否可以下架
     */
    public boolean canOffShelf() {
        return this == ON_SHELF;
    }
    
    /**
     * 判断是否可以删除
     */
    public boolean canDelete() {
        return this == DRAFT || this == OFF_SHELF;
    }
    
    /**
     * 判断是否已上架
     */
    public boolean isOnShelf() {
        return this == ON_SHELF;
    }
    
    /**
     * 判断是否已删除
     */
    public boolean isDeleted() {
        return this == DELETED;
    }
}
