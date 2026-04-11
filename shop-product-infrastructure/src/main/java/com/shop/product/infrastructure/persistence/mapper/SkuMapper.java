package com.shop.product.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.product.infrastructure.persistence.entity.SkuPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * SKU Mapper
 * 
 * @author DDD架构师
 * @since 1.0.0
 */
@Mapper
public interface SkuMapper extends BaseMapper<SkuPO> {
    
    /**
     * 根据SKU编码查询
     */
    @Select("SELECT * FROM t_sku WHERE sku_code = #{skuCode} AND deleted = 0")
    SkuPO selectBySkuCode(@Param("skuCode") String skuCode);
    
    /**
     * 根据商品ID查询SKU列表
     */
    @Select("SELECT * FROM t_sku WHERE product_id = #{productId} AND deleted = 0")
    List<SkuPO> selectByProductId(@Param("productId") Long productId);
    
    /**
     * 根据商品ID和属性签名查询SKU
     */
    @Select("SELECT * FROM t_sku WHERE product_id = #{productId} AND attribute_signature = #{attributeSignature} AND deleted = 0")
    SkuPO selectByProductIdAndAttributeSignature(@Param("productId") Long productId, 
                                                   @Param("attributeSignature") String attributeSignature);
    
    /**
     * 根据商品ID统计SKU数量
     */
    @Select("SELECT COUNT(*) FROM t_sku WHERE product_id = #{productId} AND deleted = 0")
    Long countByProductId(@Param("productId") Long productId);
}
