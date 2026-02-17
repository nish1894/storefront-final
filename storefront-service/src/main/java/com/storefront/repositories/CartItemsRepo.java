package com.storefront.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.storefront.entities.Cart;
import com.storefront.entities.CartItems; // Ensure this is the correct package for CartItems
import com.storefront.entities.User;

@Repository
public interface CartItemsRepo extends JpaRepository<CartItems,String>  {

    List<CartItems>findByCart(Cart cart); 

}
