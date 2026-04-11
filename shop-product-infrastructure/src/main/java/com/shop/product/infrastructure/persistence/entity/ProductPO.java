package com.shop.product.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品持久化对象
 * 对应数据库表：t_product
 * 
 * @author DDD架构师
 * @since 1.0.0
 */
@Data
@TableName("t_product")
public class ProductPO {
    
    /**
     * 商品ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 商品名称
     */
    private String name;
    
    /**
     * 分类ID
     */
    private Long categoryId;
    
    /**
     * 品牌ID
     */
    private Long brandId;
    
    /**
     * 供应商ID
     */
    private Long supplierId;
    
    /**
     * 商品状态：0-草稿，1-上架，2-下架，9-删除
     */
    private Integer status;
    
    /**
     * 主图URL
     */
    private String mainImage;
    
    /**
     * 售价
     */
    private BigDecimal price;
    
    /**
     * 市场价
     */
    private BigDecimal marketPrice;
    
    /**
     * 商品描述
     */
    private String description;
    
    /**
     * 商品详情HTML
     */
    private String detailHtml;
    
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
