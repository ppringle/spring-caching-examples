package com.example.caching.redis.billionaire;

import com.example.caching.redis.config.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        RedisProperties.class,
})
@ComponentScan(basePackages = "com.example.caching.redis")
public class TestRedisConfiguration {
//
//    private RedisServer redisServer;
//
//    @Autowired
//    public TestRedisConfiguration() {
//        this.redisServer = new RedisServer(6379);
//    }
//
//    @PostConstruct
//    public void postConstruct() {
//        redisServer.start();
//    }
//
//    @PreDestroy
//    public void preDestroy() {
//        redisServer.stop();
//    }

}
