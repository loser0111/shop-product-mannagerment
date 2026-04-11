package com.shop.product.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 库存锁定持久化对象
 * 对应数据库表：t_stock_lock
 * 
 * @author DDD架构师
 * @since 1.0.0
 */
@Data
@TableName("t_stock_lock")
public class StockLockPO {
    
    /**
     * 锁定ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 锁定编号
     */
    private String lockId;
    
    /**
     * 订单ID
     */
    private String orderId;
    
    /**
     * SKU ID
     */
    private Long skuId;
    
    /**
     * 仓库ID
     */
    private Long warehouseId;
    
    /**
     * 锁定数量
     */
    private BigDecimal quantity;
    
    /**
     * 锁定状态：1-已锁定，2-已释放，3-已确认，4-已过期
     */
    private Integer status;
    
    /**
     * 过期时间
     */
    private LocalDateTime expireTime;
    
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
