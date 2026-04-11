package com.shop.product.domain.valueobject;

import com.shop.product.domain.shared.ValueObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * 金钱值对象
 * 用于表示金额，确保金额计算的精度和业务规则
 * 
 * @author DDD架构师
 * @since 1.0.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Money implements ValueObject {
    
    /**
     * 金额
     */
    private BigDecimal amount;
    
    /**
     * 货币单位（默认CNY）
     */
    private String currency;
    
    /**
     * 创建Money对象
     */
    public static Money of(BigDecimal amount) {
        return Money.builder()
                .amount(amount)
                .currency("CNY")
                .build();
    }
    
    /**
     * 创建Money对象
     */
    public static Money of(BigDecimal amount, String currency) {
        return Money.builder()
                .amount(amount)
                .currency(currency)
                .build();
    }
    
    /**
     * 创建零金额
     */
    public static Money zero() {
        return Money.of(BigDecimal.ZERO);
    }
    
    /**
     * 加法
     */
    public Money add(Money other) {
        checkSameCurrency(other);
        return Money.builder()
                .amount(this.amount.add(other.amount))
                .currency(this.currency)
                .build();
    }
    
    /**
     * 减法
     */
    public Money subtract(Money other) {
        checkSameCurrency(other);
        return Money.builder()
                .amount(this.amount.subtract(other.amount))
                .currency(this.currency)
                .build();
    }
    
    /**
     * 乘法
     */
    public Money multiply(BigDecimal multiplier) {
        return Money.builder()
                .amount(this.amount.multiply(multiplier))
                .currency(this.currency)
                .build();
    }
    
    /**
     * 判断是否大于另一个金额
     */
    public boolean isGreaterThan(Money other) {
        checkSameCurrency(other);
        return this.amount.compareTo(other.amount) > 0;
    }
    
    /**
     * 判断是否小于另一个金额
     */
    public boolean isLessThan(Money other) {
        checkSameCurrency(other);
        return this.amount.compareTo(other.amount) < 0;
    }
    
    /**
     * 判断是否为正数
     */
    public boolean isPositive() {
        return this.amount.compareTo(BigDecimal.ZERO) > 0;
    }
    
    /**
     * 判断是否为零
     */
    public boolean isZero() {
        return this.amount.compareTo(BigDecimal.ZERO) == 0;
    }
    
    /**
     * 验证货币是否一致
     */
    private void checkSameCurrency(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Currency mismatch");
        }
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Objects.equals(amount, money.amount) && 
               Objects.equals(currency, money.currency);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }
    
    @Override
    public String toString() {
        return currency + " " + amount.toString();
    }
}
