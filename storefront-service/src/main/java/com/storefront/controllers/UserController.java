package com.storefront.controllers;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.storefront.clients.OrderClient;
import com.storefront.dto.CheckoutRequest;
import com.storefront.dto.OrderItemRequest;
import com.storefront.dto.OrderResponse;
import com.storefront.entities.CartItems;
import com.storefront.entities.User;
import com.storefront.helpers.Helper;
import com.storefront.helpers.SessionCart;
import com.storefront.services.SessionCartService;
import com.storefront.services.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private SessionCart sessionCart;

    @Autowired
    private SessionCartService sessionCartService;

    @Autowired
    private OrderClient orderClient;

    @RequestMapping("/profile")
    public String home(Model model, Authentication authentication) {

        String username = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(username);

        model.addAttribute("user", user);

        return "user/profile";
    }

    @RequestMapping("/cart")
    public String cart() {
        return "user/cart";
    }

    @GetMapping("/checkout")
    public String checkout() {
        return "user/checkout";
    }

    @PostMapping("/checkout")
    public String processCheckout(
            @RequestParam String shippingAddress,
            @RequestParam String paymentMethod,
            Authentication authentication,
            Model model) {

        String email = Helper.getEmailOfLoggedInUser(authentication);
        User user = userService.getUserByEmail(email);

        List<CartItems> cartItems = sessionCartService.getAllCartItems(sessionCart);

        if (cartItems.isEmpty()) {
            model.addAttribute("error", "Cart is empty");
            return "user/checkout";
        }

        List<OrderItemRequest> orderItems = new ArrayList<>();
        for (CartItems ci : cartItems) {
            OrderItemRequest item = new OrderItemRequest();
            item.setItemId(ci.getItems().getItemId());
            item.setItemTitle(ci.getItems().getTitle());
            item.setQuantity(ci.getQuantity());
            item.setPriceAtPurchase(ci.getItems().getPrice());
            orderItems.add(item);
        }

        CheckoutRequest request = new CheckoutRequest();
        request.setUserId(user.getUserId());
        request.setShippingAddress(shippingAddress);
        request.setPaymentMethod(paymentMethod);
        request.setItems(orderItems);

        try {
            OrderResponse orderResponse = orderClient.checkout(request);
            sessionCartService.clearCart(sessionCart);
            model.addAttribute("order", orderResponse);
            return "user/order-confirmation";
        } catch (Exception e) {
            logger.error("Checkout failed: {}", e.getMessage());
            model.addAttribute("error", "Checkout failed. Please try again.");
            return "user/checkout";
        }
    }
}
