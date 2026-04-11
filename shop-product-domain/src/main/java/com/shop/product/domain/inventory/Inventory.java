package com.shop.product.domain.inventory;

import com.shop.product.domain.event.DomainEvent;
import com.shop.product.domain.shared.AggregateRoot;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 库存聚合根
 * 管理SKU在特定仓库的库存信息，支持多仓库存管理
 * 
 * 业务规则：
 * 1. 可用库存 = 实际库存 - 锁定库存
 * 2. 锁定库存不能超过可用库存
 * 3. 库存不能为负数
 * 4. 锁仓有过期时间，过期自动释放
 * 
 * @author DDD架构师
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Inventory extends AggregateRoot<Long> {
    
    /**
     * SKU ID
     */
    private Long skuId;
    
    /**
     * 仓库ID
     */
    private Long warehouseId;
    
    /**
     * 可用库存
     */
    private BigDecimal availableStock;
    
    /**
     * 锁定库存
     */
    private BigDecimal lockedStock;
    
    /**
     * 预警阈值
     */
    private BigDecimal warningThreshold;
    
    /**
     * 初始化库存
     */
    public static Inventory create(Long skuId, Long warehouseId, 
                                    BigDecimal initialStock, BigDecimal warningThreshold) {
        Inventory inventory = new Inventory();
        inventory.setSkuId(skuId);
        inventory.setWarehouseId(warehouseId);
        inventory.setAvailableStock(initialStock);
        inventory.setLockedStock(BigDecimal.ZERO);
        inventory.setWarningThreshold(warningThreshold);
        inventory.preCreate();
        
        return inventory;
    }
    
    /**
     * 增加库存
     * 
     * @param quantity 增加数量
     */
    public void increase(BigDecimal quantity) {
        if (quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Increase quantity must be positive");
        }
        
        this.availableStock = this.availableStock.add(quantity);
        this.preUpdate();
        
        registerEvent(new InventoryChangedEvent(this.id, skuId, warehouseId, 
                quantity, InventoryChangeType.INCREASE));
        
        // 检查是否需要触发预警
        checkWarningThreshold();
    }
    
    /**
     * 减少库存
     * 
     * @param quantity 减少数量
     * @throws IllegalStateException 如果库存不足
     */
    public void decrease(BigDecimal quantity) {
        if (quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Decrease quantity must be positive");
        }
        
        if (availableStock.compareTo(quantity) < 0) {
            throw new IllegalStateException("Insufficient inventory");
        }
        
        this.availableStock = this.availableStock.subtract(quantity);
        this.preUpdate();
        
        registerEvent(new InventoryChangedEvent(this.id, skuId, warehouseId, 
                quantity, InventoryChangeType.DECREASE));
        
        // 检查是否需要触发预警
        checkWarningThreshold();
    }
    
    /**
     * 锁定库存（用于订单预占）
     * 
     * @param orderId 订单ID
     * @param quantity 锁定数量
     * @return 锁定记录
     */
    public StockLock lockStock(String orderId, BigDecimal quantity) {
        if (quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Lock quantity must be positive");
        }
        
        if (availableStock.compareTo(quantity) < 0) {
            throw new IllegalStateException("Insufficient inventory to lock");
        }
        
        // 减少可用库存，增加锁定库存
        this.availableStock = this.availableStock.subtract(quantity);
        this.lockedStock = this.lockedStock.add(quantity);
        this.preUpdate();
        
        // 创建锁定记录
        StockLock lock = StockLock.create(orderId, skuId, warehouseId, quantity);
        
        registerEvent(new StockLockedEvent(this.id, skuId, warehouseId, orderId, quantity));
        
        return lock;
    }
    
    /**
     * 释放锁定库存
     * 
     * @param quantity 释放数量
     */
    public void releaseLockedStock(BigDecimal quantity) {
        if (quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Release quantity must be positive");
        }
        
        if (lockedStock.compareTo(quantity) < 0) {
            throw new IllegalStateException("Locked stock insufficient to release");
        }
        
        this.lockedStock = this.lockedStock.subtract(quantity);
        this.availableStock = this.availableStock.add(quantity);
        this.preUpdate();
        
        registerEvent(new StockReleasedEvent(this.id, skuId, warehouseId, quantity));
    }
    
    /**
     * 扣减锁定库存（订单支付后）
     * 
     * @param quantity 扣减数量
     */
    public void deductLockedStock(BigDecimal quantity) {
        if (quantity.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deduct quantity must be positive");
        }
        
        if (lockedStock.compareTo(quantity) < 0) {
            throw new IllegalStateException("Locked stock insufficient to deduct");
        }
        
        this.lockedStock = this.lockedStock.subtract(quantity);
        this.preUpdate();
    }
    
    /**
     * 检查预警阈值
     */
    private void checkWarningThreshold() {
        if (warningThreshold != null && availableStock.compareTo(warningThreshold) <= 0) {
            registerEvent(new LowStockWarningEvent(this.id, skuId, warehouseId, 
                    availableStock, warningThreshold));
        }
    }
    
    /**
     * 获取实际库存（可用 + 锁定）
     */
    public BigDecimal getActualStock() {
        return availableStock.add(lockedStock);
    }
    
    /**
     * 判断是否库存充足
     */
    public boolean isSufficient(BigDecimal quantity) {
        return availableStock.compareTo(quantity) >= 0;
    }
}

/**
 * 库存变更类型
 */
enum InventoryChangeType {
    INCREASE("增加"),
    DECREASE("减少"),
    LOCK("锁定"),
    RELEASE("释放"),
    DEDUCT("扣减");
    
    private final String description;
    
    InventoryChangeType(String description) {
        this.description = description;
    }
}

/**
 * 库存变更事件
 */
class InventoryChangedEvent extends DomainEvent {
    
    private final Long skuId;
    private final Long warehouseId;
    private final BigDecimal quantity;
    private final InventoryChangeType changeType;
    
    public InventoryChangedEvent(Long inventoryId, Long skuId, Long warehouseId, 
                                  BigDecimal quantity, InventoryChangeType changeType) {
        super(String.valueOf(inventoryId), "Inventory");
        this.skuId = skuId;
        this.warehouseId = warehouseId;
        this.quantity = quantity;
        this.changeType = changeType;
    }
    
    // Getters...
    public Long getSkuId() { return skuId; }
    public Long getWarehouseId() { return warehouseId; }
    public BigDecimal getQuantity() { return quantity; }
    public InventoryChangeType getChangeType() { return changeType; }
}

/**
 * 库存锁定事件
 */
class StockLockedEvent extends DomainEvent {
    
    private final Long skuId;
    private final Long warehouseId;
    private final String orderId;
    private final BigDecimal quantity;
    
    public StockLockedEvent(Long inventoryId, Long skuId, Long warehouseId, 
                            String orderId, BigDecimal quantity) {
        super(String.valueOf(inventoryId), "Inventory");
        this.skuId = skuId;
        this.warehouseId = warehouseId;
        this.orderId = orderId;
        this.quantity = quantity;
    }
    
    // Getters...
    public Long getSkuId() { return skuId; }
    public Long getWarehouseId() { return warehouseId; }
    public String getOrderId() { return orderId; }
    public BigDecimal getQuantity() { return quantity; }
}

/**
 * 库存释放事件
 */
class StockReleasedEvent extends DomainEvent {
    
    private final Long skuId;
    private final Long warehouseId;
    private final BigDecimal quantity;
    
    public StockReleasedEvent(Long inventoryId, Long skuId, Long warehouseId, BigDecimal quantity) {
        super(String.valueOf(inventoryId), "Inventory");
        this.skuId = skuId;
        this.warehouseId = warehouseId;
        this.quantity = quantity;
    }
    
    // Getters...
    public Long getSkuId() { return skuId; }
    public Long getWarehouseId() { return warehouseId; }
    public BigDecimal getQuantity() { return quantity; }
}

/**
 * 低库存预警事件
 */
class LowStockWarningEvent extends DomainEvent {
    
    private final Long skuId;
    private final Long warehouseId;
    private final BigDecimal currentStock;
    private final BigDecimal warningThreshold;
    
    public LowStockWarningEvent(Long inventoryId, Long skuId, Long warehouseId, 
                                 BigDecimal currentStock, BigDecimal warningThreshold) {
        super(String.valueOf(inventoryId), "Inventory");
        this.skuId = skuId;
        this.warehouseId = warehouseId;
        this.currentStock = currentStock;
        this.warningThreshold = warningThreshold;
    }
    
    // Getters...
    public Long getSkuId() { return skuId; }
    public Long getWarehouseId() { return warehouseId; }
    public BigDecimal getCurrentStock() { return currentStock; }
    public BigDecimal getWarningThreshold() { return warningThreshold; }
}
