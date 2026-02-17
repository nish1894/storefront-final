package com.storefront.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private String orderId;
    private String userId;
    private LocalDateTime timestamp;
    private String orderStatus;
    private float totalPrice;
    private String paymentStatus;
    private String paymentMethod;
    private String shippingAddress;
    private List<OrderItemResponse> items;
}
