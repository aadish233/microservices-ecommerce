package com.microservices.ecommerce.api_gateway.routes;

import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions.setPath;

import java.net.URI;

import org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions;

@Configuration
public class Routes {
	@Bean
	public RouterFunction<ServerResponse> productServiceRoute() {
		return GatewayRouterFunctions.route("product_service")
				.route(RequestPredicates.path("/api/product"), HandlerFunctions.http("http://localhost:8080"))
				.filter(CircuitBreakerFilterFunctions.circuitBreaker("productServiceCircuitBreaker",
						URI.create("forward:/fallbackRoute")))
				.build();
	}
	
	@Bean
	public RouterFunction<ServerResponse> productServiceSwaggerRoute() {
		return GatewayRouterFunctions.route("product_service_swagger")
				.route(RequestPredicates.path("/aggregate/product-service/v3/api-docs"), HandlerFunctions.http("http://localhost:8080"))
				.filter(setPath("/api-docs"))
				.filter(CircuitBreakerFilterFunctions.circuitBreaker("productServiceSwaggerCircuitBreaker",
						URI.create("forward:/fallbackRoute")))
				.build();
	}
	
	@Bean
	public RouterFunction<ServerResponse> orderServiceRoute() {
		return GatewayRouterFunctions.route("order_service")
				.route(RequestPredicates.path("/api/order"), HandlerFunctions.http("http://localhost:8081"))
				.filter(CircuitBreakerFilterFunctions.circuitBreaker("orderServiceCircuitBreaker",
						URI.create("forward:/fallbackRoute")))
				.build();
	}
	
	@Bean
	public RouterFunction<ServerResponse> orderServiceSwaggerRoute() {
		return GatewayRouterFunctions.route("order_service_swagger")
				.route(RequestPredicates.path("/aggregate/order-service/v3/api-docs"), HandlerFunctions.http("http://localhost:8081"))
				.filter(setPath("/api-docs"))
				.filter(CircuitBreakerFilterFunctions.circuitBreaker("orderServiceSwaggerCircuitBreaker",
						URI.create("forward:/fallbackRoute")))
				.build();
	}
	
	@Bean
	public RouterFunction<ServerResponse> inventoryServiceRoute() {
		return GatewayRouterFunctions.route("inventory_service")
				.route(RequestPredicates.path("/api/inventory"), HandlerFunctions.http("http://localhost:8082"))
				.filter(CircuitBreakerFilterFunctions.circuitBreaker("inventoryServiceCircuitBreaker",
						URI.create("forward:/fallbackRoute")))
				.build();
	}
	
	@Bean
	public RouterFunction<ServerResponse> inventoryServiceSwaggerRoute() {
		return GatewayRouterFunctions.route("inventory_service_swagger")
				.route(RequestPredicates.path("/aggregate/inventory-service/v3/api-docs"), HandlerFunctions.http("http://localhost:8082"))
				.filter(setPath("/api-docs"))
				.filter(CircuitBreakerFilterFunctions.circuitBreaker("inventoryServiceSwaggerCircuitBreaker",
						URI.create("forward:/fallbackRoute")))
				.build();
	}
	
	@Bean
	public RouterFunction<ServerResponse> fallbackRoute() {
		return GatewayRouterFunctions.route("fallbackRoute")
				.GET("/fallbackRoute", request -> ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE)
						.body("Service unavailable, please try again later"))
				.build();
	}
}
