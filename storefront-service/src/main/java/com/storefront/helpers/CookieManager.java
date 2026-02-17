package com.storefront.helpers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;



@Component
public class CookieManager {

    private static final String CART_COOKIE_NAME = "cartData";
    private static final int COOKIE_MAX_AGE = 60 * 60 * 24 * 7; // 1 week

    public void saveCartToCookie(HttpServletResponse response, Map<String, Integer> cartItems) {
        StringBuilder rawValue = new StringBuilder();

        for (Map.Entry<String, Integer> entry : cartItems.entrySet()) {
            rawValue.append(entry.getKey())
                    .append(":")
                    .append(entry.getValue())
                    .append(",");
        }

        String encodedValue = URLEncoder.encode(rawValue.toString(), StandardCharsets.UTF_8);
        Cookie cookie = new Cookie(CART_COOKIE_NAME, encodedValue);
        cookie.setMaxAge(COOKIE_MAX_AGE);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public Map<String, Integer> loadCartFromCookie(HttpServletRequest request) {
        Map<String, Integer> cartData = new HashMap<>();
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (CART_COOKIE_NAME.equals(cookie.getName())) {
                    try {
                        String decodedValue = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
                        String[] items = decodedValue.split(",");

                        for (String item : items) {
                            if (!item.isEmpty()) {
                                String[] parts = item.split(":");
                                if (parts.length == 2) {
                                    cartData.put(parts[0], Integer.parseInt(parts[1]));
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace(); // Optionally log or handle error
                    }
                    break;
                }
            }
        }

        return cartData;
    }

    public void deleteCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(CART_COOKIE_NAME, "");
        cookie.setMaxAge(0); // Delete the cookie
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}

// @Component
// public class CookieManager {
    
//     private static final String CART_COOKIE_NAME = "cartData";
//     private static final int COOKIE_MAX_AGE = 60 * 60 * 24 * 7; // 1 week
    
//     public void saveCartToCookie(HttpServletResponse response, Map<String, Integer> cartItems) {
//         StringBuilder cookieValue = new StringBuilder();
        
//         for (Map.Entry<String, Integer> entry : cartItems.entrySet()) {
//             cookieValue.append(entry.getKey())
//                       .append(":")
//                       .append(entry.getValue())
//                       .append(",");
//         }
        
//         Cookie cookie = new Cookie(CART_COOKIE_NAME, cookieValue.toString());
//         cookie.setMaxAge(COOKIE_MAX_AGE);
//         cookie.setPath("/");
//         response.addCookie(cookie);
//     }
    
//     public Map<String, Integer> loadCartFromCookie(HttpServletRequest request) {
//         Map<String, Integer> cartData = new HashMap<>();
//         Cookie[] cookies = request.getCookies();
        
//         if (cookies != null) {
//             for (Cookie cookie : cookies) {
//                 if (CART_COOKIE_NAME.equals(cookie.getName())) {
//                     String cookieValue = cookie.getValue();
//                     String[] items = cookieValue.split(",");
                    
//                     for (String item : items) {
//                         if (!item.isEmpty()) {
//                             String[] parts = item.split(":");
//                             if (parts.length == 2) {
//                                 String itemId = parts[0];
//                                 int quantity = Integer.parseInt(parts[1]);
//                                 cartData.put(itemId, quantity);
//                             }
//                         }
//                     }
//                     break;
//                 }
//             }
//         }
        
//         return cartData;
//     }
    
//     public void deleteCookie(HttpServletResponse response) {
//         Cookie cookie = new Cookie(CART_COOKIE_NAME, "");
//         cookie.setMaxAge(0); // Delete the cookie
//         cookie.setPath("/");
//         response.addCookie(cookie);
//     }
// }
