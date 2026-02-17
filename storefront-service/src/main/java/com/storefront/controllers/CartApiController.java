package com.storefront.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import com.storefront.dto.ItemRequest;
import com.storefront.entities.CartItems;
import com.storefront.helpers.CookieManager;
import com.storefront.helpers.SessionCart;
import com.storefront.services.CartService;
import com.storefront.services.SessionCartService;
import com.storefront.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/cart")
public class CartApiController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CartService cartService;

    @Autowired
    private SessionCart sessionCart;

    @Autowired
    private CookieManager cookieManager;

    @Autowired
    private UserService userService;

    @Autowired
    private SessionCartService sessionCartService;

    @PostMapping("/update")
    public ResponseEntity<?> updateCart(
            @RequestBody ItemRequest item,
            HttpServletResponse response,
            HttpServletRequest request,
            Authentication authentication) {

        logger.info("Processing cart update: Item ID: {} (action: {})", item.getItemId(), item.getAction());

        // Process the action
        if (item.getAction().equals("add")) {
            sessionCartService.addItem(sessionCart, item.getItemId());
        } else if (item.getAction().equals("sub")) {
            sessionCartService.subtractItem(sessionCart, item.getItemId());
        } else if (item.getAction().equals("remove")) {
            sessionCartService.removeItem(sessionCart, item.getItemId());
        } else if (item.getAction().equals("clear")) {
            sessionCartService.clearCart(sessionCart);
            cookieManager.deleteCookie(response);
            Map<String, Object> empty = new HashMap<>();
            empty.put("cartId", sessionCartService.getCartId(sessionCart));
            empty.put("itemCount", 0);
            empty.put("totalItems", 0);
            empty.put("totalPrice", 0.0f);
            empty.put("itemQuantity", 0);
            empty.put("originalPrice", 0.0f);
            empty.put("savings", 0.0f);
            empty.put("tax", 0.0f);
            empty.put("delivery", 0.0f);
            empty.put("grandTotal", 0.0f);
            return ResponseEntity.ok(empty);
        }

        // Save to cookie for guest users
        Map<String, Integer> cartSummary = sessionCartService.printCartItemsSummary(sessionCart);
        if (authentication == null) {
            cookieManager.saveCartToCookie(response, cartSummary);
        }

        // Find the specific item's current quantity after the update
        int itemQuantity = 0;
        for (CartItems ci : sessionCart.getCart().getCartItems()) {
            if (ci.getItems().getItemId().equals(item.getItemId())) {
                itemQuantity = ci.getQuantity();
                break;
            }
        }

        // Calculate order summary (same formula as CartRootController)
        float cartTotal = sessionCartService.getTotalPrice(sessionCart);
        DecimalFormat df = new DecimalFormat("0.00");
        float originalPrice = Float.parseFloat(df.format(cartTotal * 1.20f));
        float savings = Float.parseFloat(df.format(originalPrice * 0.30f));
        float discountedPrice = Float.parseFloat(df.format(originalPrice - savings));
        float tax = Float.parseFloat(df.format(discountedPrice * 0.19f));
        float delivery = discountedPrice > 0 && discountedPrice < 500 ? 100.0f : 0.0f;
        float grandTotal = Float.parseFloat(df.format(discountedPrice + tax + delivery));

        Map<String, Object> result = new HashMap<>();
        result.put("cartId", sessionCartService.getCartId(sessionCart));
        result.put("itemCount", sessionCartService.getItemCount(sessionCart));
        result.put("totalItems", sessionCartService.getTotalItems(sessionCart));
        result.put("totalPrice", cartTotal);
        result.put("itemQuantity", itemQuantity);
        result.put("originalPrice", originalPrice);
        result.put("savings", savings);
        result.put("tax", tax);
        result.put("delivery", delivery);
        result.put("grandTotal", grandTotal);

        return ResponseEntity.ok(result);
    }

    @GetMapping
    public ResponseEntity<?> getCartContents() {
        return ResponseEntity.ok(Map.of(
            "cartId", sessionCartService.getCartId(sessionCart),
            "items", sessionCartService.getAllCartItems(sessionCart),
            "itemCount", sessionCartService.getItemCount(sessionCart),
            "totalItems", sessionCartService.getTotalItems(sessionCart),
            "totalPrice", sessionCartService.getTotalPrice(sessionCart)
        ));
    }
}
