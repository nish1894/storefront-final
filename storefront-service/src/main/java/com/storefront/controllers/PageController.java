package com.storefront.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.storefront.entities.User;
import com.storefront.forms.UserForm;
import com.storefront.helpers.Message;
import com.storefront.helpers.MessageType;
import com.storefront.services.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;


@Controller
public class PageController {

    @Autowired
    private UserService userService;

    @RequestMapping("/home")
    public String home(Model model){

        System.out.println("home page handler ");
        model.addAttribute("name", "Nishant");
        return "home"; 
    }

    @RequestMapping("/contact")
    public String contact(){

        System.out.println("contact page handler ");

        return "contact"; 
    }

    @RequestMapping("/login")
    public String login(){

        System.out.println("login page handler ");

        return "login"; 
    }

    @RequestMapping("/register")
    public String register(Model model){
        UserForm userForm = new UserForm();

        userForm.setDOB(java.time.LocalDate.parse("2001-01-01"));

        model.addAttribute("userForm",userForm);


        return "register"; 
    }

    // processing register
    @RequestMapping(value = "/do-register", method =RequestMethod.POST)
    public String processRegister(@Valid @ModelAttribute UserForm userForm, BindingResult rBindingResult, HttpSession session){
        System.out.println("Processing Registration form");

        //validate form 
        if(rBindingResult.hasErrors()){
            System.out.println("loads of errors loads loads");
            return "register";
        }

        User user = new User();
        
        user.setFirstName(userForm.getFirstName());
        user.setLastName(userForm.getLastName());
        user.setEmail(userForm.getEmail());
        user.setPhoneNumber(userForm.getPhoneNumber());
        user.setPassword(userForm.getPassword());
        user.setDOB(userForm.getDOB());

        // User savedUser = userService.saveUser(user);
        // System.out.println("user saved with ID: " + savedUser.getUserId());

        // Message message =Message.builder().content("Registration successful").type(MessageType.Green).build(); 

        // session.setAttribute("message", message);

        try {
            userService.saveUser(user);
            Message message =Message.builder()
                                .content("Registration successful")
                                .type(MessageType.Green)
                                .build(); 
            
            session.setAttribute("message", message);
            return "redirect:/register"; 
        } 
        catch (DataIntegrityViolationException ex) {
            Message message = Message.builder()
                                 .content("Registration failed: Email already exists")
                                 .type(MessageType.Red)
                                 .build();
            
            session.setAttribute("message", message);
            return "redirect:/register"; 
         }


    }

    

}
 