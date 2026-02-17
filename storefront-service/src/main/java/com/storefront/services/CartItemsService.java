package com.storefront.services;

import java.util.List;

import com.storefront.entities.CartItems;
import com.storefront.entities.Items;

public interface CartItemsService {


    CartItems add(Items item); 


    void addItem(String userId, String itemId); 
    

    void deleteItem(String userId, String itemId); 


    //delete cart
    void delete(String id);

    // get all cart items
    List<CartItems> getAll();

    // get cart 
    CartItems getCartItemsByUserId(String cartItemId); 

    //update cart items
    void updateCartItemPrice(CartItems cartItem);

    // if cart item exists
    
}
