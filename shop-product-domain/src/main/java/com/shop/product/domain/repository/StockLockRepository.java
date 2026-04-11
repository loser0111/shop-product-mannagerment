package com.shop.product.domain.repository;

import com.shop.product.domain.inventory.StockLock;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 库存锁定仓储接口
 * 
 * @author DDD架构师
 * @since 1.0.0
 */
public interface StockLockRepository {
    
    /**
     * 根据锁定ID查询
     */
    Optional<StockLock> findByLockId(String lockId);
    
    /**
     * 保存锁定记录
     */
    StockLock save(StockLock stockLock);
    
    /**
     * 根据订单ID查询锁定记录
     */
    List<StockLock> findByOrderId(String orderId);
    
    /**
     * 根据SKU ID查询锁定记录
     */
    List<StockLock> findBySkuId(Long skuId);
    
    /**
     * 查询过期锁定记录
     */
    List<StockLock> findExpiredLocks(LocalDateTime now);
    
    /**
     * 根据状态查询锁定记录
     */
    List<StockLock> findByStatus(Integer status);
}
