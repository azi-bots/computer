package com.computer.ecommerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * Redis配置类
 */
@Configuration
public class RedisConfig {

    /**
     * 配置RedisTemplate
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(factory);

        // 使用GenericJackson2JsonRedisSerializer来序列化和反序列化redis的value值（包含类型信息）
        GenericJackson2JsonRedisSerializer jsonRedisSerializer = getJsonRedisSerializer();

        // 设置key的序列化方式
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        // 设置value的序列化方式
        template.setValueSerializer(jsonRedisSerializer);
        template.setHashValueSerializer(jsonRedisSerializer);

        template.afterPropertiesSet();
        return template;
    }

    /**
     * 配置Redis缓存管理器
     */
    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory factory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                // 设置缓存默认过期时间：1小时
                .entryTtl(Duration.ofHours(1))
                // 禁用缓存空值
                .disableCachingNullValues()
                // 设置key序列化方式
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                // 设置value序列化方式
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(getJsonRedisSerializer()));

        // Spring Boot 3 使用 RedisCacheManager.builder()
        return RedisCacheManager.builder(factory)
                .cacheDefaults(config)
                // 为不同的缓存设置不同的过期时间
                .withCacheConfiguration("user", RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(30)))
                .withCacheConfiguration("userAddress", RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(30)))
                .withCacheConfiguration("product", RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(10)))
                .withCacheConfiguration("category", RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(2)))
                .withCacheConfiguration("order", RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(15)))
                .withCacheConfiguration("orderItem", RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(15)))
                .withCacheConfiguration("cart", RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(5)))
                .withCacheConfiguration("payment", RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(15)))
                .withCacheConfiguration("inventory", RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(5)))
                .build();
    }

    /**
     * 获取JSON序列化器实例（使用GenericJackson2JsonRedisSerializer，包含类型信息）
     */
    private GenericJackson2JsonRedisSerializer getJsonRedisSerializer() {
        return new GenericJackson2JsonRedisSerializer();
    }
}