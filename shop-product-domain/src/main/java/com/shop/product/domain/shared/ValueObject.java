package com.shop.product.domain.shared;

import java.io.Serializable;

/**
 * 值对象标记接口
 * 值对象的特征：
 * 1. 没有唯一标识
 * 2. 通过属性值判断相等
 * 3. 不可变性（创建后不能修改）
 * 
 * @author DDD架构师
 * @since 1.0.0
 */
public interface ValueObject extends Serializable {
    
    /**
     * 值对象应该实现equals和hashCode方法
     * 基于所有属性值进行比较
     */
}
