package com.shop.product.domain.service;

import com.shop.product.domain.repository.SkuRepository;
import com.shop.product.domain.sku.Sku;
import com.shop.product.domain.sku.SkuAttribute;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * SKU领域服务
 * 处理SKU相关的复杂业务逻辑
 * 
 * @author DDD架构师
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class SkuDomainService {
    
    private final SkuRepository skuRepository;
    
    /**
     * 最大SKU数量限制
     */
    private static final int MAX_SKU_COUNT = 100;
    
    /**
     * 验证SKU属性组合是否重复
     * 
     * @param productId 商品ID
     * @param attributes 属性组合
     * @param excludeSkuId 排除的SKU ID（编辑时使用）
     * @return true表示属性组合已存在
     */
    public boolean isAttributeCombinationDuplicate(Long productId, List<SkuAttribute> attributes, Long excludeSkuId) {
        String attributeSignature = generateAttributeSignature(attributes);
        
        return skuRepository.findByProductIdAndAttributeSignature(productId, attributeSignature)
                .filter(sku -> !sku.getId().equals(excludeSkuId))
                .isPresent();
    }
    
    /**
     * 检查SKU数量是否超过限制
     * 
     * @param productId 商品ID
     * @return true表示已超过限制
     */
    public boolean isSkuCountExceeded(Long productId) {
        long count = skuRepository.countByProductId(productId);
        return count >= MAX_SKU_COUNT;
    }
    
    /**
     * 生成属性签名
     * 
     * @param attributes 属性列表
     * @return 属性签名
     */
    public String generateAttributeSignature(List<SkuAttribute> attributes) {
        if (attributes == null || attributes.isEmpty()) {
            return "";
        }
        
        StringBuilder signature = new StringBuilder();
        attributes.stream()
                .sorted((a, b) -> a.getAttributeId().compareTo(b.getAttributeId()))
                .forEach(attr -> {
                    if (signature.length() > 0) {
                        signature.append("|");
                    }
                    signature.append(attr.getAttributeId()).append(":").append(attr.getAttributeValue());
                });
        
        return signature.toString();
    }
    
    /**
     * 验证SKU编码是否唯一
     * 
     * @param skuCode SKU编码
     * @param excludeSkuId 排除的SKU ID
     * @return true表示编码已存在
     */
    public boolean isSkuCodeExists(String skuCode, Long excludeSkuId) {
        return skuRepository.findBySkuCode(skuCode)
                .filter(sku -> !sku.getId().equals(excludeSkuId))
                .isPresent();
    }
    
    /**
     * 批量生成SKU
     * 
     * @param productId 商品ID
     * @param attributeGroups 属性组合列表
     * @return 生成的SKU列表
     */
    // TODO: 实现SKU组合生成算法
    // public List<Sku> generateSkus(Long productId, List<List<SkuAttribute>> attributeGroups) {
    //     // 使用笛卡尔积算法生成所有SKU组合
    // }
}
