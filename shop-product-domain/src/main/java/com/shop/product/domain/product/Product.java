package com.shop.product.domain.product;

import com.shop.product.domain.event.DomainEvent;
import com.shop.product.domain.shared.AggregateRoot;
import com.shop.product.domain.valueobject.Money;
import com.shop.product.domain.valueobject.ProductStatus;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 商品聚合根
 * 商品领域的核心聚合根，包含商品基础信息和生命周期管理
 * 
 * 业务规则：
 * 1. 商品名称在同一分类下不能重复
 * 2. 上架商品必须包含至少一个SKU
 * 3. 商品下架后不能购买
 * 4. 商品删除前必须下架
 * 
 * @author DDD架构师
 * @since 1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class Product extends AggregateRoot<Long> {
    
    /**
     * 商品名称
     */
    private String name;
    
    /**
     * 分类ID
     */
    private Long categoryId;
    
    /**
     * 品牌ID
     */
    private Long brandId;
    
    /**
     * 供应商ID
     */
    private Long supplierId;
    
    /**
     * 商品状态
     */
    private ProductStatus status;
    
    /**
     * 主图URL
     */
    private String mainImage;
    
    /**
     * 售价
     */
    private Money price;
    
    /**
     * 市场价
     */
    private Money marketPrice;
    
    /**
     * 商品描述
     */
    private String description;
    
    /**
     * 商品详情HTML
     */
    private String detailHtml;
    
    /**
     * 商品图片列表
     */
    private List<ProductImage> images = new ArrayList<>();
    
    /**
     * SKU列表
     */
    private List<Long> skuIds = new ArrayList<>();
    
    /**
     * 创建商品
     * 
     * @param name 商品名称
     * @param categoryId 分类ID
     * @param brandId 品牌ID
     * @param supplierId 供应商ID
     * @param mainImage 主图
     * @param price 售价
     * @param marketPrice 市场价
     * @param description 描述
     * @return 新创建的商品
     */
    public static Product create(String name, Long categoryId, Long brandId, 
                                  Long supplierId, String mainImage, Money price, 
                                  Money marketPrice, String description) {
        Product product = new Product();
        product.setName(name);
        product.setCategoryId(categoryId);
        product.setBrandId(brandId);
        product.setSupplierId(supplierId);
        product.setMainImage(mainImage);
        product.setPrice(price);
        product.setMarketPrice(marketPrice);
        product.setDescription(description);
        product.setStatus(ProductStatus.DRAFT);
        product.preCreate();
        
        // 发布商品创建事件
        product.registerEvent(new ProductCreatedEvent(product.getId()));
        
        return product;
    }
    
    /**
     * 更新商品信息
     */
    public void update(String name, Long categoryId, Long brandId, 
                       Long supplierId, String mainImage, Money price, 
                       Money marketPrice, String description) {
        // 只有草稿或下架状态可以编辑
        if (!status.canOnShelf()) {
            throw new IllegalStateException("Cannot update product in status: " + status);
        }
        
        this.name = name;
        this.categoryId = categoryId;
        this.brandId = brandId;
        this.supplierId = supplierId;
        this.mainImage = mainImage;
        this.price = price;
        this.marketPrice = marketPrice;
        this.description = description;
        this.preUpdate();
        
        // 发布商品更新事件
        registerEvent(new ProductUpdatedEvent(this.id));
    }
    
    /**
     * 上架商品
     * 
     * @throws IllegalStateException 如果商品状态不允许上架
     */
    public void onShelf() {
        if (!status.canOnShelf()) {
            throw new IllegalStateException("Cannot on shelf product in status: " + status);
        }
        
        // 上架前必须包含SKU
        if (skuIds == null || skuIds.isEmpty()) {
            throw new IllegalStateException("Cannot on shelf product without SKU");
        }
        
        this.status = ProductStatus.ON_SHELF;
        this.preUpdate();
        
        // 发布商品上架事件
        registerEvent(new ProductStatusChangedEvent(this.id, ProductStatus.ON_SHELF));
    }
    
    /**
     * 下架商品
     * 
     * @throws IllegalStateException 如果商品状态不允许下架
     */
    public void offShelf() {
        if (!status.canOffShelf()) {
            throw new IllegalStateException("Cannot off shelf product in status: " + status);
        }
        
        this.status = ProductStatus.OFF_SHELF;
        this.preUpdate();
        
        // 发布商品下架事件
        registerEvent(new ProductStatusChangedEvent(this.id, ProductStatus.OFF_SHELF));
    }
    
    /**
     * 删除商品
     * 
     * @throws IllegalStateException 如果商品状态不允许删除
     */
    public void delete() {
        if (!status.canDelete()) {
            throw new IllegalStateException("Cannot delete product in status: " + status);
        }
        
        this.status = ProductStatus.DELETED;
        this.preUpdate();
        
        // 发布商品删除事件
        registerEvent(new ProductDeletedEvent(this.id));
    }
    
    /**
     * 添加商品图片
     */
    public void addImage(String url, Integer sortOrder, Boolean isMain) {
        ProductImage image = ProductImage.builder()
                .url(url)
                .sortOrder(sortOrder)
                .isMain(isMain)
                .build();
        
        if (isMain != null && isMain) {
            // 如果设为主图，将其他图片设为非主图
            images.forEach(img -> img.setIsMain(false));
            this.mainImage = url;
        }
        
        images.add(image);
        // 按排序号排序
        images.sort((a, b) -> a.getSortOrder().compareTo(b.getSortOrder()));
    }
    
    /**
     * 移除商品图片
     */
    public void removeImage(String url) {
        images.removeIf(img -> img.getUrl().equals(url));
        
        // 如果删除的是主图，重新设置主图
        if (url.equals(mainImage) && !images.isEmpty()) {
            ProductImage firstImage = images.get(0);
            firstImage.setIsMain(true);
            this.mainImage = firstImage.getUrl();
        }
    }
    
    /**
     * 关联SKU
     */
    public void addSku(Long skuId) {
        if (!skuIds.contains(skuId)) {
            skuIds.add(skuId);
        }
    }
    
    /**
     * 解除SKU关联
     */
    public void removeSku(Long skuId) {
        skuIds.remove(skuId);
    }
    
    /**
     * 判断是否已上架
     */
    public boolean isOnShelf() {
        return status.isOnShelf();
    }
    
    /**
     * 判断是否已删除
     */
    public boolean isDeleted() {
        return status.isDeleted();
    }
}
