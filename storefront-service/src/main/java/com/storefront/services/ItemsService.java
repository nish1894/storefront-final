 package com.storefront.services;

import java.util.List;

import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.springframework.data.domain.Page;

import com.storefront.entities.Items;
import com.storefront.entities.User;

public interface ItemsService {

     // save items
     Items save(Items items);

     // update items
     Items update(Items items);
 
     // get items
     List<Items> getAll();
 
     // get item by id
     Items getById(String id);
 
     // delete items
     void delete(String id);
 
     // search items
    Page<Items> searchByTitle(String nameKeyword, int size, int page, String sortBy, String order);


    Page<Items> getItemsSortedByPrice(String direction, int page, int size);

  
    // Page<Items> searchByEmail(String emailKeyword, int size, int page, String sortBy, String order, User user);
  
 
    //  // get items by category
    //  List<Items> getByCategory(String userId);
 
     // get contacts by category
     Page<Items> getItemsByCategories(List<String> categories, int page, int size);


     Page<Items> findItemsWithFilters(String title, String sortBy, String direction, Integer page, Integer size);
     
}
