package com.storefront.orderservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.storefront.orderservice.entities.OrderItems;

@Repository
public interface OrderItemsRepository extends JpaRepository<OrderItems, String> {

    List<OrderItems> findByOrder_OrderId(String orderId);
}
