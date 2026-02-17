package com.storefront.services.impl;

import java.util.List;

import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.storefront.entities.Items;
import com.storefront.entities.User;
import com.storefront.helpers.ResourceNotFoundException;
import com.storefront.repositories.ItemsRepo;
import com.storefront.services.ItemsService;

@Service
public class ItemsServiceImpl implements ItemsService {

    @Autowired
    private ItemsRepo itemsRepo;

    @Override
    public Items save(Items items) {
        
        return itemsRepo.save(items); 
    }

    @Override
    public Items update(Items items) {
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public List<Items> getAll() {
        return itemsRepo.findAll();
    }

    @Override
    public Items getById(String id) {

        return itemsRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Item not found"));
        
    }

    @Override
    public void delete(String id) {
        var contact = itemsRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Contact not found with given id"));

        itemsRepo.delete(contact);
    }

    @Override
    public Page<Items> searchByTitle(String nameKeyword, int size, int page, String sortBy, String order) {
        Sort sort = order.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
         var pageable = PageRequest.of(page, size, sort);
         return itemsRepo.findByTitleContaining( nameKeyword, pageable);
    }

    @Override
    public Page<Items> getItemsSortedByPrice(String direction, int page, int size) {
        // Determine sort direction (default to ascending if invalid)
        Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? 
                                      Sort.Direction.DESC : Sort.Direction.ASC;
        
        // Create pageable with sort by price
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "price"));
        
        // Return sorted items
        return itemsRepo.findAll(pageable);
    }




    @Override
    public Page<Items> findItemsWithFilters(String title, String sortBy, String direction, Integer page, Integer size) {
        // Set default values if not provided
         String sortField = sortBy != null ? sortBy : "id";
         Sort.Direction sortDirection = "desc".equalsIgnoreCase(direction) ? 
                                       Sort.Direction.DESC : Sort.Direction.ASC;
         int pageNum = page != null ? page : 0;
         int pageSize = size != null ? size : 10;
         
         // Create pageable with sorting and pagination
         Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(sortDirection, sortField));
         
         // Search items or return all
         if (title != null && !title.isEmpty()) {
             return itemsRepo.findByTitleContainingIgnoreCase(title, pageable);
         } else {
             return itemsRepo.findAll(pageable);
         }
    }


    @Override
    public Page<Items> getItemsByCategories(List<String> categories, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        // If no categories are selected, return all items paginated
        if (categories == null || categories.isEmpty()) {
            return itemsRepo.findAll(pageable);
        }

        // Otherwise, filter by selected categories
        return itemsRepo.findByCategory_NameIn(categories, pageable);
    }

  

    // @Override
    // public Page<Items> getByCategory(User user, int page, int size, String sortField, String sortDirection) {
    //     Sort sort = order.equals("desc") ? Sort.by(sortField).descending() : Sort.by(sortField).ascending();

    // }

}
