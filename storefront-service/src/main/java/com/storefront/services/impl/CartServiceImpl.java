package com.storefront.services.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.storefront.entities.Cart;
import com.storefront.entities.User;
import com.storefront.helpers.ResourceNotFoundException;
import com.storefront.helpers.SessionCart;
import com.storefront.repositories.CartRepo;
import com.storefront.services.CartService;
import com.storefront.services.UserService;

@Service
public class CartServiceImpl implements CartService {


    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private UserService userService; 


    @Override
    public Optional<Cart> updateCart(Cart cart) {

        Cart cart2 = cartRepo.findById(cart.getCartId())
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        cart2.setUpdatedOn(cart.getUpdatedOn());
        cart2.setItemCount(cart.getItemCount());
        cart2.setTotalPrice(cart.getTotalPrice());
     

        // save the user in database

        Cart save = cartRepo.save(cart2);
        return Optional.ofNullable(save);

    }


    @Override
    public Cart getCartByUserId(String userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCartByUserId'");
    }


    @Override
    public boolean isCartExistsByUserId(String userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isCartExistsByUserId'");
    }


    @Override
    public Cart addCart(String userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addCart'");
    }


    @Override
    public SessionCart initializeSessionCart(SessionCart cart) {
        // Check if the cart is already initialized
            // Create a new cart
            // cart = new Cart();
            // cart.setCartId(UUID.randomUUID().toString());
            // cart.setUpdatedOn(LocalDateTime.now());
            // cart.setItemCount(0);
            // cart.setTotalPrice(0.0f);
            // cart.setCartItems(new ArrayList<>());
        
            throw new UnsupportedOperationException("Unimplemented method 'addCart'");

    }
    
    

    // @Override
    // public Cart getCartByUserId(String userId) {
         
    //     return cartRepo.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("Contact not found with given id"));

    // }

    // @Override
    // public boolean isCartExistsByUserId(String userId) {
    //     Cart cart2 = cartRepo.findByUserId(userId).orElse(null);
    //         return cart2 != null ? true : false;
    // }


    // @Override
    // public Cart addCart(String userId) {
    //        Optional<Cart> existingCart = cartRepo.findByUserId(userId);
    
    //     // cart already exist 
    //     if (existingCart.isPresent()) {
    //         return existingCart.get();
    //      } 
    //     else {
    //     // Cart doesn't exist, create a new one
    //     User user = userService.getUserByUserId(userId); 
        
    //     Cart newCart = Cart.builder()
    //         .cartId(UUID.randomUUID().toString())
    //         .updatedOn(LocalDateTime.now())
    //         .itemCount(0)
    //         .totalPrice(0.0f)
    //         .user(user)
    //         .cartItems(new ArrayList<>())
    //         .build();
        
    //     return cartRepo.save(newCart);
    // }
    // } 

}
