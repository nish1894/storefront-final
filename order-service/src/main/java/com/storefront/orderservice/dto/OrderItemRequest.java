package com.storefront.orderservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemRequest {

    @NotBlank(message = "Item ID is required")
    private String itemId;

    @NotBlank(message = "Item title is required")
    private String itemTitle;

    @Min(value = 1, message = "Quantity must be at least 1")
    private int quantity;

    // WARNING: In production, price should be fetched from the product service
    // and NOT trusted from the client. This is accepted here for the checkout stub.
    @Positive(message = "Price must be positive")
    private float priceAtPurchase;
}
