package com.storefront.orderservice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItems {

    @Id
    private String orderItemId;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "order_id")
    private Orders order;
    
    // Item reference replaced with itemId string
    @Column(nullable = false)
    private String itemId;
    
    @Column(nullable = false)
    private String itemTitle;
    
    @Column(nullable = false)
    private Integer quantity;
    
    @Column(nullable = false)
    private float priceAtPurchase;

}
