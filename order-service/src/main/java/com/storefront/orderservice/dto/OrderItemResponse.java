package com.storefront.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemResponse {

    private String orderItemId;
    private String itemId;
    private String itemTitle;
    private int quantity;
    private float priceAtPurchase;
}
