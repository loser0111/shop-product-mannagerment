package com.shop.product.application.service;

import com.shop.product.application.command.CreateProductCommand;
import com.shop.product.application.dto.ProductDTO;
import com.shop.product.domain.inventory.Inventory;
import com.shop.product.domain.product.Product;
import com.shop.product.domain.repository.InventoryRepository;
import com.shop.product.domain.repository.ProductRepository;
import com.shop.product.domain.repository.SkuRepository;
import com.shop.product.domain.service.SkuDomainService;
import com.shop.product.domain.sku.Sku;
import com.shop.product.domain.sku.SkuAttribute;
import com.shop.product.domain.valueobject.Money;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 商品应用服务
 * 协调领域对象完成用例，处理事务边界
 * 
 * @author DDD架构师
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductApplicationService {
    
    private final ProductRepository productRepository;
    private final SkuRepository skuRepository;
    private final InventoryRepository inventoryRepository;
    private final SkuDomainService skuDomainService;
    
    /**
     * 创建商品
     * 
     * @param command 创建商品命令
     * @return 商品ID
     */
    @Transactional
    public Long createProduct(CreateProductCommand command) {
        // 1. 验证商品名称是否重复
        if (productRepository.existsByCategoryIdAndName(command.getCategoryId(), command.getName())) {
            throw new IllegalArgumentException("Product name already exists in this category");
        }
        
        // 2. 创建商品
        Product product = Product.create(
                command.getName(),
                command.getCategoryId(),
                command.getBrandId(),
                command.getSupplierId(),
                command.getMainImage(),
                Money.of(command.getPrice()),
                command.getMarketPrice() != null ? Money.of(command.getMarketPrice()) : null,
                command.getDescription()
        );
        
        // 3. 添加商品图片
        if (command.getImages() != null) {
            command.getImages().forEach(img -> 
                product.addImage(img.getUrl(), img.getSortOrder(), img.getIsMain())
            );
        }
        
        // 4. 保存商品
        product = productRepository.save(product);
        
        // 5. 创建SKU和库存
        if (command.getSkus() != null && !command.getSkus().isEmpty()) {
            createSkusAndInventories(product.getId(), command.getSkus());
        }
        
        log.info("Product created: id={}, name={}", product.getId(), product.getName());
        
        return product.getId();
    }
    
    /**
     * 商品上架
     * 
     * @param productId 商品ID
     */
    @Transactional
    public void onShelfProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        
        product.onShelf();
        productRepository.save(product);
        
        log.info("Product on shelf: id={}", productId);
    }
    
    /**
     * 商品下架
     * 
     * @param productId 商品ID
     */
    @Transactional
    public void offShelfProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        
        product.offShelf();
        productRepository.save(product);
        
        log.info("Product off shelf: id={}", productId);
    }
    
    /**
     * 删除商品
     * 
     * @param productId 商品ID
     */
    @Transactional
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        
        product.delete();
        productRepository.save(product);
        
        log.info("Product deleted: id={}", productId);
    }
    
    /**
     * 查询商品详情
     * 
     * @param productId 商品ID
     * @return 商品DTO
     */
    // TODO: 实现查询方法，需要组装DTO
    // public ProductDTO getProductDetail(Long productId) {
    //     Product product = productRepository.findById(productId)
    //             .orElseThrow(() -> new IllegalArgumentException("Product not found"));
    //     
    //     // 查询SKU列表
    //     // 查询库存信息
    //     // 组装DTO返回
    // }
    
    /**
     * 创建SKU和库存
     */
    private void createSkusAndInventories(Long productId, List<CreateSkuCommand> skuCommands) {
        for (CreateSkuCommand skuCmd : skuCommands) {
            // 1. 验证SKU编码唯一性
            if (skuDomainService.isSkuCodeExists(skuCmd.getSkuCode(), null)) {
                throw new IllegalArgumentException("SKU code already exists: " + skuCmd.getSkuCode());
            }
            
            // 2. 转换属性
            List<SkuAttribute> attributes = skuCmd.getAttributes().stream()
                    .map(attr -> SkuAttribute.builder()
                            .attributeId(attr.getAttributeId())
                            .attributeName(attr.getAttributeName())
                            .attributeValue(attr.getAttributeValue())
                            .sortOrder(attr.getSortOrder())
                            .build())
                    .collect(Collectors.toList());
            
            // 3. 验证属性组合是否重复
            if (skuDomainService.isAttributeCombinationDuplicate(productId, attributes, null)) {
                throw new IllegalArgumentException("SKU attribute combination already exists");
            }
            
            // 4. 创建SKU
            Sku sku = Sku.create(
                    productId,
                    skuCmd.getSkuCode(),
                    skuCmd.getBarcode(),
                    attributes,
                    Money.of(skuCmd.getPrice()),
                    skuCmd.getCostPrice() != null ? Money.of(skuCmd.getCostPrice()) : null
            );
            
            sku = skuRepository.save(sku);
            
            // 5. 创建库存
            Inventory inventory = Inventory.create(
                    sku.getId(),
                    skuCmd.getWarehouseId(),
                    skuCmd.getInitialStock(),
                    BigDecimal.ZERO  // 默认预警阈值为0
            );
            
            inventoryRepository.save(inventory);
        }
    }
}
