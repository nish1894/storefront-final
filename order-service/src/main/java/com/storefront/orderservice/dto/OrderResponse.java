package com.storefront.orderservice.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.storefront.orderservice.entities.OrderStatus;
import com.storefront.orderservice.entities.PaymentMethod;
import com.storefront.orderservice.entities.PaymentStatus;

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
public class OrderResponse {

    private String orderId;
    private String userId;
    private LocalDateTime timestamp;
    private OrderStatus orderStatus;
    private float totalPrice;
    private PaymentStatus paymentStatus;
    private PaymentMethod paymentMethod;
    private String shippingAddress;
    private List<OrderItemResponse> items;
}
