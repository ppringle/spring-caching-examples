package com.example.caching;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SpringCachingWithCaffeineApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringCachingWithCaffeineApplication.class, args);
	}

}
