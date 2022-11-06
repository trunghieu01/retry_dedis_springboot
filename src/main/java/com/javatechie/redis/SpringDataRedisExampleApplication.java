package com.javatechie.redis;

import com.javatechie.redis.entity.Product;
import com.javatechie.redis.respository.ProductDao;
import io.github.resilience4j.retry.annotation.Retry;

import org.springframework.aop.ThrowsAdvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SpringBootApplication
@RestController
@RequestMapping("/")
public class SpringDataRedisExampleApplication {
	@Autowired
	private ProductDao dao;

	@PostMapping
	public Product save(@RequestBody Product product) {
		return dao.save(product);
	}

	@GetMapping
	public List<Product> getAllProducts() {
		return dao.findAll();
	}

	@GetMapping("/{id}")
	public Product findProduct(@PathVariable int id) {
		return dao.findProductById(id);
	}

	@DeleteMapping("/{id}")
	public String remove(@PathVariable int id) {
		return dao.deleteProduct(id);
	}

	int attempt = 1;
	@GetMapping("/getAll")
	// @CircuitBreaker(name =USER_SERVICE,fallbackMethod = "getAllAvailableProducts")
	@Retry(name = "services", fallbackMethod = "getAllAvailableProducts")
	public String displayOrders() throws Exception {
		System.out.println("retry method called " + attempt);
		attempt++;
		if(attempt ==1 || attempt ==2 || attempt ==3 ) {
			throw new Exception("Next");}
		return "Hi";
	}
	
	public String getAllAvailableProducts(Exception e) {
		return "Failled";
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringDataRedisExampleApplication.class, args);
	}

}
