package com.shop.product.domain.event;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 领域事件基类
 * 所有领域事件的抽象基类，提供事件的基本属性
 * 
 * @author DDD架构师
 * @since 1.0.0
 */
public abstract class DomainEvent implements Serializable {
    
    /**
     * 事件ID
     */
    private final String eventId;
    
    /**
     * 事件发生时间
     */
    private final LocalDateTime occurredOn;
    
    /**
     * 事件版本
     */
    private final int version;
    
    /**
     * 聚合根ID
     */
    private final String aggregateId;
    
    /**
     * 聚合根类型
     */
    private final String aggregateType;
    
    public DomainEvent(String aggregateId, String aggregateType) {
        this.eventId = UUID.randomUUID().toString();
        this.occurredOn = LocalDateTime.now();
        this.version = 1;
        this.aggregateId = aggregateId;
        this.aggregateType = aggregateType;
    }
    
    public DomainEvent(String aggregateId, String aggregateType, int version) {
        this.eventId = UUID.randomUUID().toString();
        this.occurredOn = LocalDateTime.now();
        this.version = version;
        this.aggregateId = aggregateId;
        this.aggregateType = aggregateType;
    }
    
    /**
     * 获取事件ID
     */
    public String getEventId() {
        return eventId;
    }
    
    /**
     * 获取事件发生时间
     */
    public LocalDateTime getOccurredOn() {
        return occurredOn;
    }
    
    /**
     * 获取事件版本
     */
    public int getVersion() {
        return version;
    }
    
    /**
     * 获取聚合根ID
     */
    public String getAggregateId() {
        return aggregateId;
    }
    
    /**
     * 获取聚合根类型
     */
    public String getAggregateType() {
        return aggregateType;
    }
}
