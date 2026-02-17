package com.storefront.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.storefront.dto.CartItemDTO;
import com.storefront.entities.CartItems;
import com.storefront.entities.Items;
import com.storefront.entities.Reviews;
import com.storefront.helpers.SessionCart;
import com.storefront.services.ItemsService;
import com.storefront.services.ReviewService;
import com.storefront.services.SessionCartService;

@Controller
@RequestMapping("/store")
public class StoreController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private ItemsService itemsService;

    @Autowired
    private SessionCart sessionCart; // Let Spring inject the managed bean

    @Autowired
    private SessionCartService sessionCartService;

    @Autowired 
    private ReviewService reviewService; 

    

    @RequestMapping("/home")
    public String home(Model model){

        List<Items> allItems = itemsService.getAll(); 

        // Print items to terminal
        // logger.info("All items retrieved: {}", allItems);
         

        model.addAttribute("items", allItems);
        
        
        return "store/home"; 
    }


    @RequestMapping("/home2")
    public String home2(Model model){

        List<Items> allItems = itemsService.getAll(); 

    
        
        model.addAttribute("items", allItems);
        
        
        return "store/home2"; 
    }



    @RequestMapping("/cart")
    public String cart(Model model){

        // List<Items> allCartItems = sessionCartService.getAllItemsOfCart(sessionCart);
        
        List<CartItems> allCartItems1 = sessionCartService.getAllCartItems(sessionCart);

        List<CartItemDTO> cartItemDTOs = CartItemDTO.fromCartItems(allCartItems1);



         // Print items to terminal
        // logger.info("All cart Items retrieved: {}", cartItemDTOs);
        // logger.info("All cart Items retrieved: {}", allCartItems);


        // Map<String, Integer> cartSummary = sessionCartService.printCartItemsSummary(sessionCart);

        // logger.info("CART SUMMARY: {}", cartSummary);



        // List<Items> allItems = itemsService.getAll(); 


        
        model.addAttribute("items", cartItemDTOs);

        
        
        return "store/cart"; 
    }

    @RequestMapping("/product/{id}")
    public String product(@PathVariable("id") String id, Model model) {
        // Fetch product by id from your service/repository
        Items product = itemsService.getById(id);
        
        // Add product to model
        model.addAttribute("product", product);
        
        return "store/product";
    }

    @GetMapping("/product/{id}")
    public String productDetails(@PathVariable String id, Model model) {
        // Fetch product by id from your service/repository
        Items product = itemsService.getById(id);
        
        // Add product to model
        model.addAttribute("product", product);
        
        // Add review data
        List<Reviews> reviews = reviewService.getReviewsByItem(id);
        double averageRating = reviewService.getAverageRatingForItem(id);
        long reviewCount = reviewService.getReviewCountForItem(id);
        Map<Integer, Long> ratingDistribution = reviewService.getRatingDistribution(id);
        
        model.addAttribute("reviews", reviews);
        model.addAttribute("averageRating", averageRating);
        model.addAttribute("reviewCount", reviewCount);
        model.addAttribute("ratingDistribution", ratingDistribution);
        
        return "store/product";
    }

    @PostMapping("/product/{id}/review")
    public String submitReview(@PathVariable String id, 
                             @RequestParam int rating,
                             @RequestParam String comment,
                             @RequestParam String reviewerName) {
        Reviews review = new Reviews();
        review.setRating(rating);
        review.setComment(comment);
        review.setReviewer(reviewerName); // Or get from authenticated user
        
        reviewService.createReview(id, review);
        
        return "redirect:/store/product/" + id;
    }

    @GetMapping("/home/search-view")
    public String searchItemsView(
            @RequestParam(required = false) String title,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            Model model) {
            
        Page<Items> itemPage = itemsService.findItemsWithFilters(
                title, sortBy, direction, page, size);
                
        model.addAttribute("items", itemPage.getContent());
        
        if (title != null && !title.isEmpty()) {
            model.addAttribute("searchTerm", title);
        }
        
        // Return the full home template instead of just a fragment
        return "store/home";
    }
    
    @GetMapping("/home/by-price-view")
    public String getItemsSortedByPriceView(
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {
            
        Page<Items> itemPage = itemsService.getItemsSortedByPrice(
                direction, page, size);
                
        model.addAttribute("items", itemPage.getContent());
        model.addAttribute("currentPage", itemPage.getNumber());
        model.addAttribute("totalItems", itemPage.getTotalElements());
        model.addAttribute("totalPages", itemPage.getTotalPages());
        
        // For building pagination URLs
        model.addAttribute("direction", direction);
        model.addAttribute("size", size);
        
        return "store/home"; // Reuse your existing template
    }

    @GetMapping("/home/by-category-view")
    public String getItemsByCategories(
            @RequestParam(required = false) List<String> categories,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Model model) {

        Page<Items> itemPage = itemsService.getItemsByCategories(categories, page, size);

        model.addAttribute("items", itemPage.getContent());
        model.addAttribute("currentPage", itemPage.getNumber());
        model.addAttribute("totalItems", itemPage.getTotalElements());
        model.addAttribute("totalPages", itemPage.getTotalPages());

        model.addAttribute("categories", categories); // For keeping checkboxes checked
        model.addAttribute("size", size);

        return "store/home";
    }

}
