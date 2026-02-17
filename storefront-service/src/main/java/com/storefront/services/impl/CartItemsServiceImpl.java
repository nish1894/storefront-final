package com.storefront.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.storefront.entities.CartItems;
import com.storefront.entities.Items;
import com.storefront.entities.User;
import com.storefront.helpers.ResourceNotFoundException;
import com.storefront.repositories.CartItemsRepo;
import com.storefront.repositories.CartRepo;
import com.storefront.repositories.ItemsRepo;
import com.storefront.services.CartItemsService;
import com.storefront.services.CartService;
import com.storefront.services.ItemsService;

import jakarta.transaction.Transactional;

@Service
public class CartItemsServiceImpl implements CartItemsService{

    @Autowired
    private CartRepo cartRepo;

    @Autowired
    private CartItemsRepo cartItemsRepo;

    @Autowired
    private ItemsService itemsService; 

    @Autowired
    private CartService cartService;



    @Override
    public void addItem(String userId, String itemId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addItem'");
    }

    @Override
    public void deleteItem(String userId, String itemId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteItem'");
    }

    @Override
    public void delete(String cartItemid) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public List<CartItems> getAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAll'");
    }

    @Override
    public CartItems getCartItemsByUserId(String cartItemid) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCartItemsByUserId'");
    }

    @Override
    public void updateCartItemPrice(CartItems cartItem) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateCartItemPrice'");
    }

    @Override
    public CartItems add(Items item) {
        CartItems cartItem = new CartItems();

        String cartItemId  = UUID.randomUUID().toString();
        cartItem.setCartItemId(cartItemId);

        cartItem.setPrice(item.getPrice());
        cartItem.setQuantity(1);
        cartItem.setTotalPrice(item.getPrice() * cartItem.getQuantity());
        cartItem.setUpdatedOn(java.time.LocalDateTime.now().toString());
        cartItem.setItems(item);
      

        return cartItemsRepo.save(cartItem);
    }


    // @Transactional
    // public void addItem(String userId, String itemId) {
    //     // Find the item
    //     Items item = itemsService.getById(itemId);


    //     if (cartService.isCartExistsByUserId(userId) == false){

    //     }

    //     String cart_id = cartService.getCartByUserId(userId).getCartId();

    //     int addQuantity = 1; 
        
    //     // Check inventory
    //     if (item.getInventory() < addQuantity) {
    //         throw new ResourceNotFoundException("Not enough inventory available. ");
    //     }
        
    //     // Check if the item is already in the cart
    //     CartItemId cartItemId = new CartItemId(cart_id, itemId);
    //     Optional<CartItems> existingCartItem = cartRepo.findById(cartItemId);
        
    //     if (existingCartItem.isPresent()) {
    //         // Update existing cart item
    //         CartItems cartItem = existingCartItem.get();
    //         cartItem.setQuantity(cartItem.getQuantity() + quantity);
    //         updateCartItemPrice(cartItem);
    //     } else {
    //         // Create new cart item
    //         CartItems newCartItem = new CartItems();
    //         newCartItem.setId(cartItemId);
    //         newCartItem.setUser(user);
    //         newCartItem.setItems(item);
    //         newCartItem.setQuantity(quantity);
    //         newCartItem.setUpdatedOn(getCurrentDateString());
    //         updateCartItemPrice(newCartItem);
            
    //         cartRepo.save(newCartItem);
    //     }
    // }

    // @Override
    // public void deleteItem(String userId, String itemId) {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'deleteItem'");
    // }




    // @Override
    // public void delete(CartItems.CartItemId id) {
    //      var contact = cartRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Contact not found with given id"));

    //     cartRepo.delete(contact);
    // }



    // @Override
    // public List<CartItems> getAll() {
    //     return cartRepo.findAll();
    // }


    // @Override
    // public CartItems getCartItemsByUserId(CartItems.CartItemId id) {

    //     return cartRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Contact not found with given id"));  
    // }



    // @Override
    // public void updateCartItemPrice(CartItems cartItem) {
    //     // Calculate total price based on item price and quantity
    //     float itemPrice = cartItem.getItems().getPrice();
        
    //     // Update the cart item with the new  price
    //     cartItem.setPrice(itemPrice);
        
    //     // Save the updated cart item
    //     cartRepo.save(cartItem);
    // }

    // @Override
    // public void addItem() {
    //     // TODO Auto-generated method stub
    //     throw new UnsupportedOperationException("Unimplemented method 'addItem'");
    // }





 

}
