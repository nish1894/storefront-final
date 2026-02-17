package com.storefront.controllers;

import org.slf4j.LoggerFactory;

import java.text.DecimalFormat;
import java.util.Map;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.storefront.helpers.CookieManager;
import com.storefront.helpers.SessionCart;
import com.storefront.services.SessionCartService;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class CartRootController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SessionCart sessionCart;

    @Autowired
    private SessionCartService sessionCartService;

    @Autowired
    private CookieManager cookieManager;

    @ModelAttribute
    public void currentCartInformation(Model model, Authentication authentication, HttpServletRequest request) {
        // Guest user — load cart from cookies if session is empty
        if (authentication == null) {
            if (sessionCartService.getItemCount(sessionCart) == 0) {
                Map<String, Integer> savedCart = cookieManager.loadCartFromCookie(request);
                for (Map.Entry<String, Integer> entry : savedCart.entrySet()) {
                    sessionCartService.addItem(sessionCart, entry.getKey());
                    if (entry.getValue() > 1) {
                        sessionCartService.updateItemQuantity(sessionCart, entry.getKey(), entry.getValue());
                    }
                }
            }
        }

        // Cart data — available for both guest and authenticated users
        float cartTotal = sessionCartService.getTotalPrice(sessionCart);

        model.addAttribute("cartItemCount", sessionCartService.getItemCount(sessionCart));
        model.addAttribute("cartTotalItems", sessionCartService.getTotalItems(sessionCart));

        // Cart quantities map (itemId → quantity) for home page quantity selectors
        model.addAttribute("cartQuantities", sessionCartService.printCartItemsSummary(sessionCart));

        // Order summary calculation
        // x = cartTotal (sum of unit price * quantity)
        // originalPrice = 1.20x   (20% markup)
        // savings        = 30% of originalPrice
        // discountedPrice = originalPrice - savings = 0.84x
        // tax            = 19% of discountedPrice
        // delivery       = $100 if discountedPrice < 500, else free
        // grandTotal     = discountedPrice + tax + delivery
        DecimalFormat df = new DecimalFormat("0.00");

        float originalPrice = Float.parseFloat(df.format(cartTotal * 1.20f));
        float savings = Float.parseFloat(df.format(originalPrice * 0.30f));
        float discountedPrice = Float.parseFloat(df.format(originalPrice - savings));
        float tax = Float.parseFloat(df.format(discountedPrice * 0.19f));
        float delivery = discountedPrice > 0 && discountedPrice < 500 ? 100.0f : 0.0f;
        float grandTotal = Float.parseFloat(df.format(discountedPrice + tax + delivery));

        model.addAttribute("originalPrice", originalPrice);
        model.addAttribute("savings", savings);
        model.addAttribute("tax", tax);
        model.addAttribute("delivery", delivery);
        model.addAttribute("cartTotalPrice", grandTotal);
    }
}
