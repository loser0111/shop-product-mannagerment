package com.shop.product.domain.service;

import com.shop.product.domain.inventory.Inventory;
import com.shop.product.domain.inventory.StockLock;
import com.shop.product.domain.repository.InventoryRepository;
import com.shop.product.domain.repository.StockLockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 库存领域服务
 * 处理库存相关的复杂业务逻辑，包括锁仓、库存分配等
 * 
 * @author DDD架构师
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class InventoryDomainService {
    
    private final InventoryRepository inventoryRepository;
    private final StockLockRepository stockLockRepository;
    
    /**
     * 锁定库存
     * 
     * @param orderId 订单ID
     * @param skuId SKU ID
     * @param warehouseId 仓库ID
     * @param quantity 锁定数量
     * @return 锁定记录
     */
    @Transactional
    public StockLock lockStock(String orderId, Long skuId, Long warehouseId, BigDecimal quantity) {
        Inventory inventory = inventoryRepository.findBySkuIdAndWarehouseId(skuId, warehouseId)
                .orElseThrow(() -> new IllegalStateException("Inventory not found"));
        
        StockLock lock = inventory.lockStock(orderId, quantity);
        
        // 保存库存和锁定记录
        inventoryRepository.save(inventory);
        stockLockRepository.save(lock);
        
        return lock;
    }
    
    /**
     * 释放锁定库存
     * 
     * @param lockId 锁定ID
     */
    @Transactional
    public void releaseStock(String lockId) {
        StockLock lock = stockLockRepository.findByLockId(lockId)
                .orElseThrow(() -> new IllegalStateException("Stock lock not found"));
        
        if (!lock.isLocked()) {
            throw new IllegalStateException("Stock lock is not in LOCKED status");
        }
        
        Inventory inventory = inventoryRepository.findBySkuIdAndWarehouseId(lock.getSkuId(), lock.getWarehouseId())
                .orElseThrow(() -> new IllegalStateException("Inventory not found"));
        
        inventory.releaseLockedStock(lock.getQuantity());
        lock.release();
        
        inventoryRepository.save(inventory);
        stockLockRepository.save(lock);
    }
    
    /**
     * 确认扣减库存（订单支付后）
     * 
     * @param lockId 锁定ID
     */
    @Transactional
    public void confirmStockDeduction(String lockId) {
        StockLock lock = stockLockRepository.findByLockId(lockId)
                .orElseThrow(() -> new IllegalStateException("Stock lock not found"));
        
        if (!lock.isLocked()) {
            throw new IllegalStateException("Stock lock is not in LOCKED status");
        }
        
        Inventory inventory = inventoryRepository.findBySkuIdAndWarehouseId(lock.getSkuId(), lock.getWarehouseId())
                .orElseThrow(() -> new IllegalStateException("Inventory not found"));
        
        inventory.deductLockedStock(lock.getQuantity());
        lock.confirm();
        
        inventoryRepository.save(inventory);
        stockLockRepository.save(lock);
    }
    
    /**
     * 多仓库存分配策略
     * 根据订单地址和库存情况分配最优仓库
     * 
     * @param skuId SKU ID
     * @param quantity 需求数量
     * @param region 地区（用于就近分配）
     * @return 分配的仓库ID，如果没有足够库存返回null
     */
    // TODO: 实现多仓库存分配算法
    // public Long allocateWarehouse(Long skuId, BigDecimal quantity, String region) {
    //     // 1. 查询所有仓库的库存
    //     // 2. 根据地区优先选择就近仓库
    //     // 3. 如果就近仓库库存不足，选择库存充足的仓库
    //     // 4. 返回最优仓库ID
    // }
    
    /**
     * 批量锁定库存
     * 
     * @param orderId 订单ID
     * @param items 锁定项列表
     * @return 是否全部锁定成功
     */
    // TODO: 实现批量锁仓
    // @Transactional
    // public boolean batchLockStock(String orderId, List<StockLockItem> items) {
    //     // 1. 验证所有SKU库存充足
    //     // 2. 批量锁定库存
    //     // 3. 任一失败则回滚
    // }
    
    /**
     * 清理过期锁定
     * 定时任务调用，释放过期的库存锁定
     */
    // TODO: 实现定时清理过期锁定
    // @Transactional
    // public void cleanExpiredLocks() {
    //     // 1. 查询所有过期的锁定记录
    //     // 2. 逐个释放锁定
    // }
}
