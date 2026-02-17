package com.storefront.dto;

import com.storefront.entities.CartItems;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
    private String cartItemId;
    private Integer quantity;
    private float price;
    private float totalPrice;
    private String updatedOn;
    
    // Item details
    private String itemId;
    private String itemTitle;
    private String itemDescription;
    private String itemImage;
    private String category;
    
    // Static method to convert CartItems to CartItemDTO
    public static CartItemDTO fromCartItem(CartItems cartItem) {
        CartItemDTO dto = new CartItemDTO();
        dto.setCartItemId(cartItem.getCartItemId());
        dto.setQuantity(cartItem.getQuantity());
        dto.setPrice(cartItem.getPrice());
        dto.setTotalPrice(cartItem.getTotalPrice());
        dto.setUpdatedOn(cartItem.getUpdatedOn());
        
        if (cartItem.getItems() != null) {
            dto.setItemId(cartItem.getItems().getItemId());
            dto.setItemTitle(cartItem.getItems().getTitle());
            dto.setItemDescription(cartItem.getItems().getDescription());
            dto.setItemImage(cartItem.getItems().getImage());
            // dto.setCategory(cartItem.getItems().getCategory());
            // Add any other item properties you need
        }

        
        
        return dto;
    }
    
    // Utility method to convert a list of CartItems to a list of DTOs
    public static List<CartItemDTO> fromCartItems(List<CartItems> cartItems) {
        return cartItems.stream()
                .map(CartItemDTO::fromCartItem)
                .collect(Collectors.toList());
    }
    @Override
    public String toString() {
        return "CartItemDTO{" +
                "cartItemId='" + cartItemId + '\'' +
                ", quantity=" + quantity +
                ", price=" + price +
                ", totalPrice=" + totalPrice +
                ", updatedOn='" + updatedOn + '\'' +
                ", itemId=" + itemId +
                ", itemTitle='" + itemTitle + '\'' +
                ", itemTitle='" + itemImage + '\'' +
                ", itemDescription='" + itemDescription + '\'' +
                "}";
}
}