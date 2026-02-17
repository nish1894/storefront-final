package com.storefront.helpers;

import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

@Component
public class SessionHelper {

    //this removes message after it is displayed in registration page so its not get stuck there 
    public static void removeMessage() {
        System.out.println("removing message from session");
        try{ HttpSession session =((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest().getSession();
            session.removeAttribute("message");

        }catch(Exception e){
            System.out.println("Error in SessionHelper: "+e);
            
            e.printStackTrace();
        }
        
        

    }

}
