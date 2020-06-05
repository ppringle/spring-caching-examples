package com.example.caching.redis.billionaire;

import org.springframework.boot.env.YamlPropertySourceLoader;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.TestPropertySource;

@Configuration
@EnableCaching
@ComponentScan(basePackages = "com.example.caching")
@TestPropertySource(value = "classpath:application.yml")
public class CacheConfig {
}
