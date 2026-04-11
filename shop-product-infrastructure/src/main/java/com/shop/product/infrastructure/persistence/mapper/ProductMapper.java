package com.shop.product.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.product.infrastructure.persistence.entity.ProductPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 商品Mapper
 * 
 * @author DDD架构师
 * @since 1.0.0
 */
@Mapper
public interface ProductMapper extends BaseMapper<ProductPO> {
    
    /**
     * 根据分类ID和名称查询商品
     */
    @Select("SELECT * FROM t_product WHERE category_id = #{categoryId} AND name = #{name} AND deleted = 0")
    ProductPO selectByCategoryIdAndName(@Param("categoryId") Long categoryId, @Param("name") String name);
    
    /**
     * 根据分类ID查询商品列表
     */
    @Select("SELECT * FROM t_product WHERE category_id = #{categoryId} AND deleted = 0")
    List<ProductPO> selectByCategoryId(@Param("categoryId") Long categoryId);
    
    /**
     * 根据品牌ID查询商品列表
     */
    @Select("SELECT * FROM t_product WHERE brand_id = #{brandId} AND deleted = 0")
    List<ProductPO> selectByBrandId(@Param("brandId") Long brandId);
    
    /**
     * 根据供应商ID查询商品列表
     */
    @Select("SELECT * FROM t_product WHERE supplier_id = #{supplierId} AND deleted = 0")
    List<ProductPO> selectBySupplierId(@Param("supplierId") Long supplierId);
}
