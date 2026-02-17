package com.storefront.services;

import java.util.List;
import java.util.Optional;

import com.storefront.entities.User;

public interface UserService {

    User saveUser(User user);
    Optional<User> getUserById(String ID);
    Optional<User> updateUser(User user);
    void deleteUser(User user);
    boolean isUserExistByEmail(String email);
    boolean isUserExistByPhoneNumber(String phoneNumber);
    List<User> getAllUser();

    User getUserByEmail(String email);
    User getUserByUserId(String userId);

    
    
    String getUserIdbyusername(String username); 

    

}
