package com.storefront.orderservice.service;

import java.util.List;

import com.storefront.orderservice.dto.CheckoutRequest;
import com.storefront.orderservice.dto.OrderResponse;

public interface OrderService {

    OrderResponse checkout(CheckoutRequest request);

    OrderResponse getOrderById(String orderId);

    List<OrderResponse> getOrdersByUserId(String userId);

    OrderResponse cancelOrder(String orderId);
}
