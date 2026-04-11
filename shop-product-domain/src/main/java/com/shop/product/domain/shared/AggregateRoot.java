package com.shop.product.domain.shared;

import com.shop.product.domain.event.DomainEvent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 聚合根基类
 * 所有聚合根的抽象基类，继承自Entity，提供领域事件支持
 * 
 * @author DDD架构师
 * @since 1.0.0
 */
public abstract class AggregateRoot<ID extends Serializable> extends Entity<ID> {

    /**
     * 领域事件列表
     */
    private transient List<DomainEvent> domainEvents = new ArrayList<>();

    /**
     * 注册领域事件
     * 
     * @param event 领域事件
     */
    protected void registerEvent(DomainEvent event) {
        if (domainEvents == null) {
            domainEvents = new ArrayList<>();
        }
        domainEvents.add(event);
    }

    /**
     * 获取所有领域事件
     * 
     * @return 领域事件列表（不可修改）
     */
    public List<DomainEvent> getDomainEvents() {
        if (domainEvents == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(domainEvents);
    }

    /**
     * 清除所有领域事件
     */
    public void clearDomainEvents() {
        if (domainEvents != null) {
            domainEvents.clear();
        }
    }

    /**
     * 判断是否有领域事件
     * 
     * @return true表示有领域事件
     */
    public boolean hasDomainEvents() {
        return domainEvents != null && !domainEvents.isEmpty();
    }
}
