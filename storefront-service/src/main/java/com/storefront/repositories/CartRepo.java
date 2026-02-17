package com.storefront.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.storefront.entities.Cart;
import com.storefront.entities.User;

public interface CartRepo extends JpaRepository<Cart, String> {

    
    List<Cart>findByUser(User user); 
    Optional<Cart> findByUser_UserId(String userId);
    


}
