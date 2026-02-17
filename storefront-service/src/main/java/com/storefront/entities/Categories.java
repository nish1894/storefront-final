package com.storefront.entities;

import java.util.ArrayList;
import java.util.List;
import com.storefront.entities.Items; // Ensure the correct package path for Items

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Categories {

    @Id
    private String categoryId;
    private String name; 


        // One-to-Many relationship with Items
    @OneToMany(mappedBy = "category", 
               fetch = FetchType.LAZY)
    private List<Items> items = new ArrayList<>();
    
}
