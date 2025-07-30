package com.microservices.ecommerce.inventory_service.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.microservices.ecommerce.inventory_service.model.Inventory;

public interface InventoryRepository extends JpaRepository<Inventory, Long>{

	boolean existsBySkuCodeAndQuantityIsGreaterThanEqual(String skuCode, Integer quantity);

}
