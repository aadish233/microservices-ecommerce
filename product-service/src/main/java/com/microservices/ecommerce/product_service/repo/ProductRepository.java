package com.microservices.ecommerce.product_service.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.microservices.ecommerce.product_service.model.Product;

public interface ProductRepository extends MongoRepository<Product, String>{

}
