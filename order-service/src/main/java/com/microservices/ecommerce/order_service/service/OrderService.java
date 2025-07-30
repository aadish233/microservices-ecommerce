package com.microservices.ecommerce.order_service.service;

import java.util.UUID;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.microservices.ecommerce.order_service.client.InventoryClient;
import com.microservices.ecommerce.order_service.dto.OrderRequest;
import com.microservices.ecommerce.order_service.event.OrderPlacedEvent;
import com.microservices.ecommerce.order_service.model.Order;
import com.microservices.ecommerce.order_service.repo.OrderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
	
	private final OrderRepository orderRepository;
	private final InventoryClient inventoryClient;
	private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;
	
	public void placeOrder(OrderRequest orderRequest) {
		boolean isProductInStock = inventoryClient.isInStock(orderRequest.skuCode(), orderRequest.quantity());
		if(isProductInStock) {
			// map OrderRequest to Order
			Order order = new Order();
			order.setOrderNumber(UUID.randomUUID().toString());
			order.setPrice(orderRequest.price());
			order.setSkuCode(orderRequest.skuCode());
			order.setQuantity(orderRequest.quantity());
		
			orderRepository.save(order);
			
			// Send message to Kafka topic
			OrderPlacedEvent orderPlacedEvent = new OrderPlacedEvent();
			orderPlacedEvent.setOrderNumber(order.getOrderNumber());
			orderPlacedEvent.setEmail(orderRequest.userDetails().email());
			orderPlacedEvent.setFirstName(orderRequest.userDetails().firstName());
			orderPlacedEvent.setLastName(orderRequest.userDetails().lastName());
			log.info("Start sending message to kafka topic: {}", orderPlacedEvent);
			kafkaTemplate.sendDefault(orderPlacedEvent);
			log.info("End of sending message to kafka topic: {}", orderPlacedEvent);
			log.info("Order placed successfully.");
		} else {
			throw new RuntimeException("Product with skuCode" + orderRequest.skuCode() + "is out of stock");
		}
	}
}
