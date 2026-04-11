package com.shop.product.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 库存持久化对象
 * 对应数据库表：t_inventory
 * 
 * @author DDD架构师
 * @since 1.0.0
 */
@Data
@TableName("t_inventory")
public class InventoryPO {
    
    /**
     * 库存ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * SKU ID
     */
    private Long skuId;
    
    /**
     * 仓库ID
     */
    private Long warehouseId;
    
    /**
     * 可用库存
     */
    private BigDecimal availableStock;
    
    /**
     * 锁定库存
     */
    private BigDecimal lockedStock;
    
    /**
     * 预警阈值
     */
    private BigDecimal warningThreshold;
    
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
    
    /**
     * 版本号（乐观锁）
     */
    @Version
    private Long version;
}
