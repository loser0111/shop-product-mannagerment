package com.shop.product.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.product.infrastructure.persistence.entity.InventoryPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.List;

/**
 * 库存Mapper
 * 
 * @author DDD架构师
 * @since 1.0.0
 */
@Mapper
public interface InventoryMapper extends BaseMapper<InventoryPO> {
    
    /**
     * 根据SKU ID和仓库ID查询库存
     */
    @Select("SELECT * FROM t_inventory WHERE sku_id = #{skuId} AND warehouse_id = #{warehouseId}")
    InventoryPO selectBySkuIdAndWarehouseId(@Param("skuId") Long skuId, @Param("warehouseId") Long warehouseId);
    
    /**
     * 根据SKU ID查询所有仓库库存
     */
    @Select("SELECT * FROM t_inventory WHERE sku_id = #{skuId}")
    List<InventoryPO> selectBySkuId(@Param("skuId") Long skuId);
    
    /**
     * 根据仓库ID查询库存列表
     */
    @Select("SELECT * FROM t_inventory WHERE warehouse_id = #{warehouseId}")
    List<InventoryPO> selectByWarehouseId(@Param("warehouseId") Long warehouseId);
    
    /**
     * 乐观锁更新库存
     */
    @Update("UPDATE t_inventory SET available_stock = #{availableStock}, locked_stock = #{lockedStock}, " +
            "version = version + 1, update_time = NOW() " +
            "WHERE id = #{id} AND version = #{version}")
    int updateWithVersion(InventoryPO inventory);
    
    /**
     * 查询库存预警列表
     */
    @Select("SELECT * FROM t_inventory WHERE available_stock <= warning_threshold")
    List<InventoryPO> selectWarningStocks();
    
    /**
     * 获取SKU总库存
     */
    @Select("SELECT SUM(available_stock) FROM t_inventory WHERE sku_id = #{skuId}")
    BigDecimal getTotalStockBySkuId(@Param("skuId") Long skuId);
}
