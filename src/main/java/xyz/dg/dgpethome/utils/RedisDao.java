package xyz.dg.dgpethome.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @program: dgpethome
 * @description:
 * @author: ruihao_ji
 * @create: 2022-06-13 16:28
 **/
@Component
@RequiredArgsConstructor
public class RedisDao {
    private final RedisTemplate<String, Object> redisTemplate;
    /**
     * 通过表达式使用scan方法扫描匹配的key（而不是keys）
     * @param pattern   key匹配表达式
     * @return          匹配的key集合
     */
    public Set<String> scanKeys(String pattern) {
        ScanOptions opts = ScanOptions.scanOptions().match(pattern).count(1000).build();
        Set<String> res = new HashSet<>();
        return redisTemplate.execute((RedisCallback<Set<String>>) e -> {
            e.scan(opts).forEachRemaining(r -> res.add(new String(r)));
            return res;
        });
    }

    public void setRedisCacheCode(String key, String value){
        redisTemplate.opsForValue().set(key ,value,5, TimeUnit.MINUTES);

    }

    public boolean hasRedisKey(String key){
        return redisTemplate.hasKey(key);
    }

    public String getRedisCacheCode(String key){
        return redisTemplate.opsForValue().get(key).toString();
    }
    public boolean deleteRedisKey(String key){
        return redisTemplate.delete(key);
    }
}