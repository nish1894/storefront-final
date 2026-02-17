package com.storefront.entities;

import jakarta.persistence.ForeignKey;
import java.util.ArrayList;
import java.util.List;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Items {

    @Id
    private String itemId;

    private String title;
    
    @Column(nullable = false)
    private float price;

    @Column(length = 1000)
    private String description;
    
    private String image;
    
    private int inventory;
    
    private float rating;
    
    private int ratingCount;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", foreignKey = @ForeignKey(name = "fk_category"))
    private Categories category;

    @OneToMany(mappedBy = "items", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<CartItems> cartItems = new ArrayList<>();


}