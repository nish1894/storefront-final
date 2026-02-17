package com.storefront.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

import org.hibernate.cache.spi.support.AbstractReadWriteAccess.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.storefront.entities.Categories;
import com.storefront.entities.Items;


@Repository
public interface ItemsRepo extends JpaRepository<Items, String> {

    //find the contact by user 

    Page<Items> findByCategory(Categories category, Pageable pageable);

    Page<Items> findByTitleContaining( String namekeyword, Pageable pageable);


    // Search by title (case-insensitive contains)
    Page<Items> findByTitleContainingIgnoreCase(String namekeyword, Pageable pageable);
    
    
    // Get all items with pagination and sorting applied
    Page<Items> findAll(Pageable pageable);


    Page<Items> findByCategory_NameIn(List<String> names, Pageable pageable);

}
