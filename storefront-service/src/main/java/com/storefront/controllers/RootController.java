package com.storefront.controllers;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.storefront.entities.User;
import com.storefront.helpers.Helper;
import com.storefront.services.UserService;

@ControllerAdvice
public class RootController {

    private Logger logger  = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UserService userService;

    @ModelAttribute
    public void addLogidInUserInformation(Model model, Authentication authentication){
        if(authentication == null) return; 

        System.out.println("Adding logged in user information to the model");

        String username =  Helper.getEmailOfLoggedInUser(authentication);


        logger.info("User Profile in: {}", username);

        //fetch data from database 

        User user = userService.getUserByEmail(username);

        if ( user == null){
            model.addAttribute("loggedInUser", null);
        }

        else{
            System.out.println(user.getName());
            System.out.println(user.getEmail());
    
            model.addAttribute("loggedInUser", user);
        }

        }

    }


