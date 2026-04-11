package com.shop.product.domain.shared;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 领域实体基类
 * 所有领域实体的抽象基类，提供通用的标识和行为
 * 
 * @author DDD架构师
 * @since 1.0.0
 */
public abstract class Entity<ID extends Serializable> {

    /**
     * 实体唯一标识
     */
    protected ID id;

    /**
     * 创建时间
     */
    protected LocalDateTime createTime;

    /**
     * 更新时间
     */
    protected LocalDateTime updateTime;

    /**
     * 版本号（乐观锁）
     */
    protected Long version;

    /**
     * 获取实体ID
     */
    public ID getId() {
        return id;
    }

    /**
     * 设置实体ID
     */
    public void setId(ID id) {
        this.id = id;
    }

    /**
     * 获取创建时间
     */
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     */
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新时间
     */
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     */
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取版本号
     */
    public Long getVersion() {
        return version;
    }

    /**
     * 设置版本号
     */
    public void setVersion(Long version) {
        this.version = version;
    }

    /**
     * 判断两个实体是否相等（基于ID）
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity<?> entity = (Entity<?>) o;
        return Objects.equals(id, entity.id);
    }

    /**
     * 计算哈希码（基于ID）
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    /**
     * 判断实体是否为新创建的
     */
    public boolean isNew() {
        return id == null;
    }

    /**
     * 更新操作前的回调
     */
    public void preUpdate() {
        this.updateTime = LocalDateTime.now();
    }

    /**
     * 创建操作前的回调
     */
    public void preCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createTime = now;
        this.updateTime = now;
        this.version = 0L;
    }
}
