package com.microservices.ecommerce.order_service.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservices.ecommerce.order_service.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{

}
