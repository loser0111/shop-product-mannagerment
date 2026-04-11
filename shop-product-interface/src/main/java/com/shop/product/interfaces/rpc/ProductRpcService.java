package com.shop.product.interfaces.rpc;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品RPC服务接口
 * 提供给其他微服务调用的Dubbo接口
 * 
 * @author DDD架构师
 * @since 1.0.0
 */
public interface ProductRpcService {
    
    /**
     * 根据ID查询商品
     * 
     * @param productId 商品ID
     * @return 商品信息
     */
    ProductDTO getProductById(Long productId);
    
    /**
     * 根据SKU ID查询SKU信息
     * 
     * @param skuId SKU ID
     * @return SKU信息
     */
    SkuDTO getSkuById(Long skuId);
    
    /**
     * 批量查询SKU信息
     * 
     * @param skuIds SKU ID列表
     * @return SKU信息列表
     */
    List<SkuDTO> batchGetSkus(List<Long> skuIds);
    
    /**
     * 查询SKU库存
     * 
     * @param skuId SKU ID
     * @return 库存数量
     */
    BigDecimal getSkuStock(Long skuId);
    
    /**
     * 锁定库存
     * 
     * @param orderId 订单ID
     * @param skuId SKU ID
     * @param quantity 锁定数量
     * @return 锁定ID
     */
    String lockStock(String orderId, Long skuId, BigDecimal quantity);
    
    /**
     * 释放锁定库存
     * 
     * @param lockId 锁定ID
     * @return 是否成功
     */
    Boolean releaseStock(String lockId);
    
    /**
     * 扣减库存（订单支付后）
     * 
     * @param lockId 锁定ID
     * @return 是否成功
     */
    Boolean deductStock(String lockId);
    
    /**
     * 恢复库存（订单取消/退款）
     * 
     * @param skuId SKU ID
     * @param quantity 恢复数量
     * @return 是否成功
     */
    Boolean restoreStock(Long skuId, BigDecimal quantity);
    
    /**
     * 检查商品是否上架
     * 
     * @param productId 商品ID
     * @return true表示已上架
     */
    Boolean isProductOnShelf(Long productId);
    
    /**
     * 检查SKU是否可用
     * 
     * @param skuId SKU ID
     * @return true表示可用
     */
    Boolean isSkuAvailable(Long skuId);
    
    /**
     * 根据商品ID查询SKU列表
     * 
     * @param productId 商品ID
     * @return SKU列表
     */
    List<SkuDTO> getSkusByProductId(Long productId);
}

/**
 * 商品DTO（RPC用）
 */
class ProductDTO implements java.io.Serializable {
    private Long id;
    private String name;
    private Long categoryId;
    private Long brandId;
    private Integer status;
    private String mainImage;
    private BigDecimal price;
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }
    public Long getBrandId() { return brandId; }
    public void setBrandId(Long brandId) { this.brandId = brandId; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getMainImage() { return mainImage; }
    public void setMainImage(String mainImage) { this.mainImage = mainImage; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
}

/**
 * SKU DTO（RPC用）
 */
class SkuDTO implements java.io.Serializable {
    private Long id;
    private Long productId;
    private String skuCode;
    private String barcode;
    private BigDecimal price;
    private Integer status;
    private BigDecimal stock;
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getSkuCode() { return skuCode; }
    public void setSkuCode(String skuCode) { this.skuCode = skuCode; }
    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public BigDecimal getStock() { return stock; }
    public void setStock(BigDecimal stock) { this.stock = stock; }
}
