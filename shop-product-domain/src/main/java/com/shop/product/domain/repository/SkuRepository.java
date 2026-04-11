package com.shop.product.domain.repository;

import com.shop.product.domain.sku.Sku;

import java.util.List;
import java.util.Optional;

/**
 * SKU仓储接口
 * 定义SKU聚合根的持久化操作
 * 
 * @author DDD架构师
 * @since 1.0.0
 */
public interface SkuRepository {
    
    /**
     * 根据ID查询SKU
     */
    Optional<Sku> findById(Long id);
    
    /**
     * 保存SKU
     */
    Sku save(Sku sku);
    
    /**
     * 批量保存SKU
     */
    List<Sku> saveAll(List<Sku> skus);
    
    /**
     * 根据SKU编码查询
     */
    Optional<Sku> findBySkuCode(String skuCode);
    
    /**
     * 根据商品ID查询SKU列表
     */
    List<Sku> findByProductId(Long productId);
    
    /**
     * 根据商品ID和属性签名查询SKU
     */
    Optional<Sku> findByProductIdAndAttributeSignature(Long productId, String attributeSignature);
    
    /**
     * 检查SKU编码是否已存在
     */
    boolean existsBySkuCode(String skuCode);
    
    /**
     * 根据商品ID统计SKU数量
     */
    long countByProductId(Long productId);
    
    /**
     * 根据ID列表查询SKU
     */
    List<Sku> findByIdIn(List<Long> ids);
}
