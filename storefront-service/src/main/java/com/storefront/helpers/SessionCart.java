package com.storefront.helpers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.storefront.entities.Cart;
import com.storefront.entities.CartItems;
import com.storefront.entities.Items;
import com.storefront.entities.User;
import com.storefront.services.ItemsService;

import lombok.Getter;


@Component
@Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class SessionCart implements Serializable {
    private static final long serialVersionUID = 1L;

    private final String cartId;
    private Cart cart;

    public SessionCart() {
        this.cartId = UUID.randomUUID().toString();
        this.cart = new Cart();
        this.cart.setCartItems(new java.util.ArrayList<>());
    }

    /**
     * Returns the unique identifier for this session's cart.
     */
    public String getCartId() {
        return cartId;
    }

    /**
     * Returns the Cart entity associated with this session.
     */
    public Cart getCart() {
        return cart;
    }

    /**
     * Replaces the current Cart entity (e.g., when loading from persistence).
     */
    public void setCart(Cart cart) {
        this.cart = cart;
        if (cart.getCartItems() != null) {
            // keep cartItems in sync
            cart.setCartItems(cart.getCartItems());
        }
    }

    /**
     * Returns the list of CartItems in the current Cart.
     */
    public List<CartItems> getCartItems() {
        return cart.getCartItems();
    }
}

// @Component
// @Scope(value = WebApplicationContext.SCOPE_SESSION, proxyMode = ScopedProxyMode.TARGET_CLASS)
// public class SessionCart implements Serializable {
//     private static final long serialVersionUID = 1L;
    
//     @Autowired
//     private ItemsService itemsService;

    
    
//     private String cartId;
//     private Cart cart;
//     @Getter
//     private List<CartItems> cartItems = new ArrayList<>();
    
//     public SessionCart() {
//         this.cartId = UUID.randomUUID().toString();
//         this.cart = new Cart(); // Create new Cart entity
//         this.cart.setCartItems(new ArrayList<>());
//     }
    
//     public String getCartId() {
//         return cartId;
//     }
    
//     public Cart getCart() {
//         return cart;
//     }
    
//     public void setCart(Cart cart) {
//         this.cart = cart;
//         if (cart.getCartItems() != null) {
//             this.cartItems = cart.getCartItems();
//         }
//     }
    
//     public void addItem(String itemId) {
//         // Check if the item already exists in the cart
//         for (CartItems cartItem : cartItems) {
//             if (cartItem.getItems().getItemId().equals(itemId)) {
//                 // Item already exists, update quantity
//                 cartItem.setQuantity(cartItem.getQuantity() + 1);
//                 return;
//             }
//         }
        
//         // Add the item to the cart entity
//         Items item = itemsService.getById(itemId);
//         if (item != null) {
//             CartItems cartItem = new CartItems();
//             cartItem.setCartItemId(UUID.randomUUID().toString());
//             cartItem.setCart(cart);   
//             cartItem.setQuantity(1);
//             cartItem.setItems(item);
            
//             // Add to both lists to keep them in sync
//             cartItems.add(cartItem);
//             cart.getCartItems().add(cartItem);
            
//             System.out.println("Item added to cart: " + cartItem.getItems().getItemId());
//         } else {
//             System.out.println("Item not found: " + itemId);
//         }
//     }
    
//     public void removeItem(String itemId) {
//         CartItems itemToRemove = null;
//         for (CartItems cartItem : cartItems) {
//             if (cartItem.getItems().getItemId().equals(itemId)) {
//                 itemToRemove = cartItem;
//                 break;
//             }
//         }
        
//         if (itemToRemove != null) {
//             cartItems.remove(itemToRemove);
//             cart.getCartItems().remove(itemToRemove);
//             System.out.println("Item removed from cart: " + itemId);
//         }
//     }
    
//     public void updateItemQuantity(String itemId, int quantity) {
//         for (CartItems cartItem : cartItems) {
//             if (cartItem.getItems().getItemId().equals(itemId)) {
//                 cartItem.setQuantity(quantity);
//                 System.out.println("Updated quantity for item " + itemId + " to " + quantity);
//                 return;
//             }
//         }
//     }
    
//     // number of items in cart 
//     public int getItemCount() {
//         System.out.println("Item count: " + cartItems.size());
//         return cartItems.size();
//     }
    
//     public int getTotalItems() {
//         int total = 0;
//         for (CartItems item : cartItems) {
//             total += item.getQuantity();
//         }
//         return total;
//     }
    
//     @Override
//     public String toString() {
//         return "SessionCart{cartId='" + cartId + "', itemCount=" + getItemCount() + "}";
//     }


//     // retrun all cart items 
//     public List<CartItems> getAllCartItems() {
//         for (CartItems cartItem : cartItems) {
//             System.out.println("Item ID: " + cartItem.getItems().getItemId()+ "  Quantity: " + cartItem.getQuantity());
//         }

//         return cartItems;
//     }

//     @JsonIgnore
//     public List<Items> getAllItemsOfCart() {
      
        
//         List<Items>ItemsOfCart = new ArrayList<>();
//         for (CartItems cartItem : cartItems) {
//             Items item = cartItem.getItems();
//             ItemsOfCart.add(item);
//         }

//         return ItemsOfCart;
//     }

//     public Map<String, Integer>  printCartItemsSummary() {
//         Map<String, Integer> myMap = new HashMap<>();

//         for (CartItems cartItem : cartItems) {
//             String a1 = cartItem.getItems().getItemId();
//             int b1 = cartItem.getQuantity();
//             myMap.put(a1, b1);
//         }

//         for (Map.Entry<String, Integer> entry : myMap.entrySet()) {
//             System.out.println("itemId: " + entry.getKey() + ", Quantity: " + entry.getValue());
//         }
//         return myMap; 
//     }

//     public int getTotalPrice() {
//         int total = 0;
//         for (CartItems item : cartItems) {
//             total += item.getItems().getPrice() * item.getQuantity();
//         }
//         return total;
//     }
//     public void clearCart() {
//         cartItems.clear();
//         cart.getCartItems().clear();
//         System.out.println("Cart cleared");
//     }
//     public void setUser(User user) {
//         cart.setUser(user);
//     }
//     public User getUser() {
//         return cart.getUser();
//     }


    
    
// }

// public class SessionCart implements Serializable {
//     private static final long serialVersionUID = 1L;
    
//     private String cartId;
    
    
//     public SessionCart() {
//         this.cartId = UUID.randomUUID().toString();
//     }
    
//     public String getCartId() {
//         return cartId;
//     }
    
//     @Override
//     public String toString() {
//         return "SessionCart{cartId='" + cartId + "'}";
//     }


//     private List<CartItems> cartItems = new ArrayList<>();


// }
