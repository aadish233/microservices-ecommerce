package com.microservices.ecommerce.order_service;

import static org.hamcrest.MatcherAssert.assertThat;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import com.microservices.ecommerce.order_service.stub.InventoryClientStub;

import io.restassured.RestAssured;

@Testcontainers
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
class OrderServiceApplicationTests {
	@ServiceConnection
	static MySQLContainer<?> mySQLContainer = new MySQLContainer<>(DockerImageName.parse("mysql:8.3.0"));
	
	@LocalServerPort
	private Integer port;
	
	static {
		mySQLContainer.start();
	}
	
	@BeforeEach
	void setup() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}
	

	@Test
	void shouldPlaceOrder() {
		String orderRequest = """
				{
					"skuCode": "iphone_15",
					"quantity": 1,
					"price": 1200
				}
				""";
		
		InventoryClientStub.stubInventoryCall("iphone_15", 1);
		
		String responseString = RestAssured.given()
				.contentType("application/json")
				.body(orderRequest)
				.when()
				.post("/api/order")
				.then()
				.statusCode(201)
				.extract()
				.body().asString();
		
		assertThat(responseString, Matchers.is("Order placed successfully"));
	}

}
