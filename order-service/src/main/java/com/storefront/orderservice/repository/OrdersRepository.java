package com.storefront.orderservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.storefront.orderservice.entities.Orders;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, String> {

    List<Orders> findByUserIdOrderByTimestampDesc(String userId);
}
