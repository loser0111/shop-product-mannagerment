package com.shop.product.domain.repository;

import com.shop.product.domain.inventory.Inventory;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * 库存仓储接口
 * 定义库存聚合根的持久化操作
 * 
 * @author DDD架构师
 * @since 1.0.0
 */
public interface InventoryRepository {
    
    /**
     * 根据ID查询库存
     */
    Optional<Inventory> findById(Long id);
    
    /**
     * 保存库存
     */
    Inventory save(Inventory inventory);
    
    /**
     * 根据SKU ID和仓库ID查询库存
     */
    Optional<Inventory> findBySkuIdAndWarehouseId(Long skuId, Long warehouseId);
    
    /**
     * 根据SKU ID查询所有仓库库存
     */
    List<Inventory> findBySkuId(Long skuId);
    
    /**
     * 根据仓库ID查询库存列表
     */
    List<Inventory> findByWarehouseId(Long warehouseId);
    
    /**
     * 根据SKU ID列表查询库存
     */
    List<Inventory> findBySkuIdIn(List<Long> skuIds);
    
    /**
     * 乐观锁更新库存
     * 
     * @param inventory 库存对象
     * @return 是否更新成功
     */
    boolean updateWithVersion(Inventory inventory);
    
    /**
     * 批量更新库存
     */
    // TODO: 实现批量更新方法
    // void batchUpdate(List<Inventory> inventories);
    
    /**
     * 查询库存预警列表
     */
    List<Inventory> findWarningStocks();
    
    /**
     * 获取SKU总库存（所有仓库）
     */
    BigDecimal getTotalStockBySkuId(Long skuId);
}
