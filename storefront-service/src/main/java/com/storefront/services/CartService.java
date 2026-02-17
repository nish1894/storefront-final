package com.storefront.services;

import java.util.Optional;

import com.storefront.entities.Cart;
import com.storefront.helpers.SessionCart;

public interface CartService {

  
    //update cart 
    Optional<Cart> updateCart(Cart cart);

    Cart getCartByUserId(String userId);

    boolean isCartExistsByUserId(String userId); 
    
    Cart addCart(String userId);

    

    // // Delete cart
    // void delete(CartItems.CartItemId id);

    // // Get all cart items
    // List<CartItems> getAll();

    // // Get cart
    // CartItems getCartItemsByUserId(CartItems.CartItemId id);

    // // Update cart items
    // void updateCartItemPrice(CartItems cartItem);


    // session cart 
    SessionCart initializeSessionCart(SessionCart cart);

}
