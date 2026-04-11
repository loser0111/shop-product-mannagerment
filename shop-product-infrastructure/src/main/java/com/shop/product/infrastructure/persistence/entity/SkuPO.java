package com.shop.product.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * SKU持久化对象
 * 对应数据库表：t_sku
 * 
 * @author DDD架构师
 * @since 1.0.0
 */
@Data
@TableName("t_sku")
public class SkuPO {
    
    /**
     * SKU ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 所属商品ID
     */
    private Long productId;
    
    /**
     * SKU编码
     */
    private String skuCode;
    
    /**
     * 条形码
     */
    private String barcode;
    
    /**
     * 属性组合JSON
     */
    private String attributesJson;
    
    /**
     * 属性签名（用于去重）
     */
    private String attributeSignature;
    
    /**
     * 售价
     */
    private BigDecimal price;
    
    /**
     * 成本价
     */
    private BigDecimal costPrice;
    
    /**
     * SKU状态：0-禁用，1-启用
     */
    private Integer status;
    
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
    
    /**
     * 逻辑删除标志
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;
}
