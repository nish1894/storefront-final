package com.storefront.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutRequest {

    private String userId;
    private String shippingAddress;
    private String paymentMethod;
    private List<OrderItemRequest> items;
}
