package com.shop.product.domain.inventory;

import com.shop.product.domain.shared.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 库存锁定实体
 * 记录订单对库存的预占信息
 * 
 * @author DDD架构师
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class StockLock extends Entity<Long> {
    
    /**
     * 锁定ID
     */
    private String lockId;
    
    /**
     * 订单ID
     */
    private String orderId;
    
    /**
     * SKU ID
     */
    private Long skuId;
    
    /**
     * 仓库ID
     */
    private Long warehouseId;
    
    /**
     * 锁定数量
     */
    private BigDecimal quantity;
    
    /**
     * 锁定状态
     */
    private LockStatus status;
    
    /**
     * 过期时间
     */
    private LocalDateTime expireTime;
    
    /**
     * 创建锁定记录
     */
    public static StockLock create(String orderId, Long skuId, Long warehouseId, BigDecimal quantity) {
        StockLock lock = new StockLock();
        lock.setLockId(UUID.randomUUID().toString());
        lock.setOrderId(orderId);
        lock.setSkuId(skuId);
        lock.setWarehouseId(warehouseId);
        lock.setQuantity(quantity);
        lock.setStatus(LockStatus.LOCKED);
        // 默认15分钟过期
        lock.setExpireTime(LocalDateTime.now().plusMinutes(15));
        lock.preCreate();
        
        return lock;
    }
    
    /**
     * 释放锁定
     */
    public void release() {
        if (this.status != LockStatus.LOCKED) {
            throw new IllegalStateException("Lock is not in LOCKED status");
        }
        
        this.status = LockStatus.RELEASED;
        this.preUpdate();
    }
    
    /**
     * 确认扣减（订单支付后）
     */
    public void confirm() {
        if (this.status != LockStatus.LOCKED) {
            throw new IllegalStateException("Lock is not in LOCKED status");
        }
        
        this.status = LockStatus.CONFIRMED;
        this.preUpdate();
    }
    
    /**
     * 判断是否已过期
     */
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expireTime);
    }
    
    /**
     * 判断是否已锁定
     */
    public boolean isLocked() {
        return status == LockStatus.LOCKED;
    }
}

/**
 * 锁定状态枚举
 */
enum LockStatus {
    LOCKED(1, "已锁定"),
    RELEASED(2, "已释放"),
    CONFIRMED(3, "已确认"),
    EXPIRED(4, "已过期");
    
    private final int code;
    private final String description;
    
    LockStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }
}
