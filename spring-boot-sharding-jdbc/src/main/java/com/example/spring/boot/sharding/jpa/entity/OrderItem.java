package com.example.spring.boot.sharding.jpa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "t_order_item")
public class OrderItem implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name = "order_item_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderItemId;
    
    @Column(name = "order_id")
    private long orderId;
    
    @Column(name = "user_id")
    private long userId;
    
    public long getOrderItemId() {
        return orderItemId;
    }
    
    public void setOrderItemId(final long orderItemId) {
        this.orderItemId = orderItemId;
    }
    
    public long getOrderId() {
        return orderId;
    }
    
    public void setOrderId(final long orderId) {
        this.orderId = orderId;
    }
    
    public long getUserId() {
        return userId;
    }
    
    public void setUserId(final long userId) {
        this.userId = userId;
    }
    
    @Override
    public String toString() {
        return String.format("order_item_id: %s, order_id: %s, user_id: %s", orderItemId, orderId, userId);
    }
}
