package com.shop.product.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 商品图片持久化对象
 * 对应数据库表：t_product_image
 * 
 * @author DDD架构师
 * @since 1.0.0
 */
@Data
@TableName("t_product_image")
public class ProductImagePO {
    
    /**
     * 图片ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    
    /**
     * 商品ID
     */
    private Long productId;
    
    /**
     * 图片URL
     */
    private String url;
    
    /**
     * 排序号
     */
    private Integer sortOrder;
    
    /**
     * 是否主图：0-否，1-是
     */
    private Integer isMain;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
