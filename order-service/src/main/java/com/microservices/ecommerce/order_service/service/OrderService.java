package com.microservices.ecommerce.order_service.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.microservices.ecommerce.order_service.dto.OrderRequest;
import com.microservices.ecommerce.order_service.model.Order;
import com.microservices.ecommerce.order_service.repo.OrderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
	
	private final OrderRepository orderRepository;
	
	public void placeOrder(OrderRequest orderRequest) {
		// map OrderRequest to Order
		Order order = new Order();
		order.setOrderNumber(UUID.randomUUID().toString());
		order.setPrice(orderRequest.price());
		order.setSkuCode(orderRequest.skuCode());
		order.setQuantity(orderRequest.quantity());
		
		orderRepository.save(order);
		
		log.info("Order placed successfully.");
	}
}
