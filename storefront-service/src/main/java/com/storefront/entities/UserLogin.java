// package com.storefront.entities;

// import jakarta.persistence.Column;
// import jakarta.persistence.Entity;
// import jakarta.persistence.Id;
// import jakarta.persistence.OneToOne;
// import lombok.AllArgsConstructor;
// import lombok.Builder;
// import lombok.Getter;
// import lombok.NoArgsConstructor;
// import lombok.Setter;
// import jakarta.persistence.CascadeType;
// import jakarta.persistence.FetchType;


// @Entity// default is class name
// @Getter
// @Setter
// @NoArgsConstructor
// @AllArgsConstructor
// @Builder
// public class UserLogin {

//     @Id
//     private String userId; 

//     @Column(unique = true, nullable = false)
//     private String email;
//     private String password;

//     private String phoneNumber;

//     private String emailVerified;
//     private String phoneVerified; 

//     private Providers provider = Providers.SELF;
//     private String providerUserID; 

//     @OneToOne(mappedBy = "userLogin", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
//     private User user; 

// }
