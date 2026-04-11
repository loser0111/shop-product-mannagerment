package com.shop.product.domain.repository;

import com.shop.product.domain.product.Product;

import java.util.List;
import java.util.Optional;

/**
 * 商品仓储接口
 * 定义商品聚合根的持久化操作
 * 
 * @author DDD架构师
 * @since 1.0.0
 */
public interface ProductRepository {
    
    /**
     * 根据ID查询商品
     */
    Optional<Product> findById(Long id);
    
    /**
     * 根据ID查询商品（包含已删除）
     */
    Optional<Product> findByIdIncludeDeleted(Long id);
    
    /**
     * 保存商品
     */
    Product save(Product product);
    
    /**
     * 根据分类ID和名称查询商品
     */
    Optional<Product> findByCategoryIdAndName(Long categoryId, String name);
    
    /**
     * 检查商品名称是否已存在
     */
    boolean existsByCategoryIdAndName(Long categoryId, String name);
    
    /**
     * 根据分类ID查询商品列表
     */
    List<Product> findByCategoryId(Long categoryId);
    
    /**
     * 根据品牌ID查询商品列表
     */
    List<Product> findByBrandId(Long brandId);
    
    /**
     * 根据供应商ID查询商品列表
     */
    List<Product> findBySupplierId(Long supplierId);
    
    /**
     * 根据状态查询商品列表
     */
    List<Product> findByStatus(Integer status);
    
    /**
     * 分页查询商品
     */
    // TODO: 实现分页查询方法
    // Page<Product> findByPage(ProductQueryCondition condition, Pageable pageable);
}
