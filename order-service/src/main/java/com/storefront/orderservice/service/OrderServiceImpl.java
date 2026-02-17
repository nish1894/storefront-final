package com.storefront.orderservice.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.storefront.orderservice.dto.CheckoutRequest;
import com.storefront.orderservice.dto.OrderItemRequest;
import com.storefront.orderservice.dto.OrderItemResponse;
import com.storefront.orderservice.dto.OrderResponse;
import com.storefront.orderservice.entities.OrderItems;
import com.storefront.orderservice.entities.OrderStatus;
import com.storefront.orderservice.entities.Orders;
import com.storefront.orderservice.entities.PaymentStatus;
import com.storefront.orderservice.repository.OrdersRepository;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    private final OrdersRepository ordersRepository;

    public OrderServiceImpl(OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    @Override
    @Transactional
    public OrderResponse checkout(CheckoutRequest request) {

        // TODO: In production, fetch item prices from the product service via REST call
        // instead of trusting client-submitted prices. Current implementation accepts
        // priceAtPurchase from the request for the checkout stub.

        float totalPrice = 0;
        for (OrderItemRequest item : request.getItems()) {
            totalPrice += item.getQuantity() * item.getPriceAtPurchase();
        }

        String orderId = UUID.randomUUID().toString();

        Orders order = Orders.builder()
                .orderId(orderId)
                .userId(request.getUserId())
                .timestamp(LocalDateTime.now())
                .orderStatus(OrderStatus.CREATED)
                .totalPrice(totalPrice)
                .paymentStatus(PaymentStatus.PENDING)
                .paymentMethod(request.getPaymentMethod())
                .shippingAddress(request.getShippingAddress())
                .build();

        for (OrderItemRequest itemReq : request.getItems()) {
            OrderItems orderItem = new OrderItems();
            orderItem.setOrderItemId(UUID.randomUUID().toString());
            orderItem.setOrder(order);
            orderItem.setItemId(itemReq.getItemId());
            orderItem.setItemTitle(itemReq.getItemTitle());
            orderItem.setQuantity(itemReq.getQuantity());
            orderItem.setPriceAtPurchase(itemReq.getPriceAtPurchase());
            order.getOrderItems().add(orderItem);
        }

        // Stub payment processing — no real integration
        log.info("Payment processing stubbed for order: {}", orderId);

        Orders saved = ordersRepository.save(order);
        return mapToResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse getOrderById(String orderId) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Order not found: " + orderId));
        return mapToResponse(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByUserId(String userId) {
        return ordersRepository.findByUserIdOrderByTimestampDesc(userId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    @Transactional
    public OrderResponse cancelOrder(String orderId) {
        Orders order = ordersRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Order not found: " + orderId));

        if (order.getOrderStatus() != OrderStatus.CREATED
                && order.getOrderStatus() != OrderStatus.PROCESSING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Order cannot be cancelled in status: " + order.getOrderStatus());
        }

        order.setOrderStatus(OrderStatus.CANCELLED);
        Orders saved = ordersRepository.save(order);
        return mapToResponse(saved);
    }

    private OrderResponse mapToResponse(Orders order) {
        List<OrderItemResponse> itemResponses = order.getOrderItems()
                .stream()
                .map(item -> OrderItemResponse.builder()
                        .orderItemId(item.getOrderItemId())
                        .itemId(item.getItemId())
                        .itemTitle(item.getItemTitle())
                        .quantity(item.getQuantity())
                        .priceAtPurchase(item.getPriceAtPurchase())
                        .build())
                .toList();

        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .userId(order.getUserId())
                .timestamp(order.getTimestamp())
                .orderStatus(order.getOrderStatus())
                .totalPrice(order.getTotalPrice())
                .paymentStatus(order.getPaymentStatus())
                .paymentMethod(order.getPaymentMethod())
                .shippingAddress(order.getShippingAddress())
                .items(itemResponses)
                .build();
    }
}
