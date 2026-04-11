package com.shop.product.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.shop.product.domain.product.Product;
import com.shop.product.domain.repository.ProductRepository;
import com.shop.product.infrastructure.persistence.entity.ProductPO;
import com.shop.product.infrastructure.persistence.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 商品仓储实现
 * 
 * @author DDD架构师
 * @since 1.0.0
 */
@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {
    
    private final ProductMapper productMapper;
    
    @Override
    public Optional<Product> findById(Long id) {
        ProductPO po = productMapper.selectById(id);
        return Optional.ofNullable(po).map(this::convertToDomain);
    }
    
    @Override
    public Optional<Product> findByIdIncludeDeleted(Long id) {
        // TODO: 实现包含已删除商品的查询
        return findById(id);
    }
    
    @Override
    public Product save(Product product) {
        ProductPO po = convertToPO(product);
        
        if (product.isNew()) {
            productMapper.insert(po);
            product.setId(po.getId());
        } else {
            productMapper.updateById(po);
        }
        
        return product;
    }
    
    @Override
    public Optional<Product> findByCategoryIdAndName(Long categoryId, String name) {
        ProductPO po = productMapper.selectByCategoryIdAndName(categoryId, name);
        return Optional.ofNullable(po).map(this::convertToDomain);
    }
    
    @Override
    public boolean existsByCategoryIdAndName(Long categoryId, String name) {
        return productMapper.selectByCategoryIdAndName(categoryId, name) != null;
    }
    
    @Override
    public List<Product> findByCategoryId(Long categoryId) {
        List<ProductPO> pos = productMapper.selectByCategoryId(categoryId);
        return pos.stream().map(this::convertToDomain).collect(Collectors.toList());
    }
    
    @Override
    public List<Product> findByBrandId(Long brandId) {
        List<ProductPO> pos = productMapper.selectByBrandId(brandId);
        return pos.stream().map(this::convertToDomain).collect(Collectors.toList());
    }
    
    @Override
    public List<Product> findBySupplierId(Long supplierId) {
        List<ProductPO> pos = productMapper.selectBySupplierId(supplierId);
        return pos.stream().map(this::convertToDomain).collect(Collectors.toList());
    }
    
    @Override
    public List<Product> findByStatus(Integer status) {
        QueryWrapper<ProductPO> wrapper = new QueryWrapper<>();
        wrapper.eq("status", status);
        List<ProductPO> pos = productMapper.selectList(wrapper);
        return pos.stream().map(this::convertToDomain).collect(Collectors.toList());
    }
    
    /**
     * PO转Domain
     */
    private Product convertToDomain(ProductPO po) {
        Product product = new Product();
        product.setId(po.getId());
        product.setName(po.getName());
        product.setCategoryId(po.getCategoryId());
        product.setBrandId(po.getBrandId());
        product.setSupplierId(po.getSupplierId());
        // TODO: 转换状态枚举
        // product.setStatus(ProductStatus.of(po.getStatus()));
        product.setMainImage(po.getMainImage());
        // TODO: 转换Money
        // product.setPrice(Money.of(po.getPrice()));
        // product.setMarketPrice(Money.of(po.getMarketPrice()));
        product.setDescription(po.getDescription());
        product.setDetailHtml(po.getDetailHtml());
        product.setCreateTime(po.getCreateTime());
        product.setUpdateTime(po.getUpdateTime());
        product.setVersion(po.getVersion());
        return product;
    }
    
    /**
     * Domain转PO
     */
    private ProductPO convertToPO(Product product) {
        ProductPO po = new ProductPO();
        po.setId(product.getId());
        po.setName(product.getName());
        po.setCategoryId(product.getCategoryId());
        po.setBrandId(product.getBrandId());
        po.setSupplierId(product.getSupplierId());
        // TODO: 转换状态
        // po.setStatus(product.getStatus().getCode());
        po.setMainImage(product.getMainImage());
        // TODO: 转换Money
        // po.setPrice(product.getPrice().getAmount());
        // po.setMarketPrice(product.getMarketPrice().getAmount());
        po.setDescription(product.getDescription());
        po.setDetailHtml(product.getDetailHtml());
        po.setCreateTime(product.getCreateTime());
        po.setUpdateTime(product.getUpdateTime());
        po.setVersion(product.getVersion());
        return po;
    }
}
