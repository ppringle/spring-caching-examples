package com.example.caching;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
@EnableCaching
public class SpringCachingWithCaffeineApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCachingWithCaffeineApplication.class, args);
    }

}
