package com.shop.product.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.shop.product.infrastructure.persistence.entity.StockLockPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 库存锁定Mapper
 * 
 * @author DDD架构师
 * @since 1.0.0
 */
@Mapper
public interface StockLockMapper extends BaseMapper<StockLockPO> {
    
    /**
     * 根据锁定ID查询
     */
    @Select("SELECT * FROM t_stock_lock WHERE lock_id = #{lockId}")
    StockLockPO selectByLockId(@Param("lockId") String lockId);
    
    /**
     * 根据订单ID查询锁定记录
     */
    @Select("SELECT * FROM t_stock_lock WHERE order_id = #{orderId}")
    List<StockLockPO> selectByOrderId(@Param("orderId") String orderId);
    
    /**
     * 根据SKU ID查询锁定记录
     */
    @Select("SELECT * FROM t_stock_lock WHERE sku_id = #{skuId}")
    List<StockLockPO> selectBySkuId(@Param("skuId") Long skuId);
    
    /**
     * 查询过期锁定记录
     */
    @Select("SELECT * FROM t_stock_lock WHERE expire_time < #{now} AND status = 1")
    List<StockLockPO> selectExpiredLocks(@Param("now") LocalDateTime now);
    
    /**
     * 根据状态查询锁定记录
     */
    @Select("SELECT * FROM t_stock_lock WHERE status = #{status}")
    List<StockLockPO> selectByStatus(@Param("status") Integer status);
}
