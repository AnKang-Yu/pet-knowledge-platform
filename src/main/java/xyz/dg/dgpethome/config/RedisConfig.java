package xyz.dg.dgpethome.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;

import java.time.Duration;
import java.util.Objects;

/**
 * @author Dugong
 * @date 2021-11-14 20:01
 * @description
 **/
@Configuration
@RequiredArgsConstructor
public class RedisConfig {
    private final LettuceConnectionFactory factory;

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setConnectionFactory(factory);
        return template;
    }

    @Bean
    public RedisCacheManager cacheManager(LettuceConnectionFactory factory) {
        return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(
                Objects.requireNonNull(redisTemplate().getConnectionFactory()))
                .cacheDefaults(getCacheConfig())
                .withCacheConfiguration("path", getCacheConfig().entryTtl(Duration.ofHours(12)))
                .build();
    }

    /**
     * 获取一个Redis缓存配置
     * @return  Redis缓存配置
     */
    private RedisCacheConfiguration getCacheConfig() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(redisTemplate().getValueSerializer())
                );
    }
}

