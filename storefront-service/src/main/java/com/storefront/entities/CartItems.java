package com.storefront.entities;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class CartItems {

    @Id
    private String cartItemId; 

    private Integer quantity;
    private float price;
    private float totalPrice;
    private String updatedOn;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cart_id")
    @JsonIgnoreProperties("cartItems") // Prevent circular reference with Cart
    private Cart cart;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id")
    @JsonIgnoreProperties("cartItems") // Prevent circular reference with Items
    private Items items;

    // Getters and setters...

    // // Optional: Generate ID like cartId_itemId
    // public void generateId() {
    //     if (cart != null && items != null) {
    //         this.id = cart.getId() + "_" + items.getItemId();
    //     }
    // }

    @Override
    public String toString() {
    return "CartItems{" +
           "cartItemId='" + cartItemId + '\'' +
           ", quantity=" + quantity +
           ", price=" + price +
           ", totalPrice=" + totalPrice +
           ", updatedOn='" + updatedOn + '\'' +
           ", itemId=" + (items != null ? items.getItemId() : null) +
           '}';
}
}
