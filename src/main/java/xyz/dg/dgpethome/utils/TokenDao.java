package xyz.dg.dgpethome.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * @program: dgpethome
 * @description:
 * @author: ruihao_ji
 * @create: 2022-06-13 16:29
 **/
@Component
@RequiredArgsConstructor
public class TokenDao {
    private static final String TOKEN_KEY = "token_";
    private final RedisTemplate<String, String> redisTemplate;
    private final RedisDao redisDao;

    /**
     * 获取用户鉴权token在redis中的key
     * @param username token对应的用户名
     * @param token token
     * @return key
     */
    public static String getTokenKey(String username, String token) {
        return TOKEN_KEY + username + "::" + SecureUtils.getMd5(token);
    }

    /**
     * 添加用户鉴权token到Redis缓存
     * @param username token对应的用户名
     * @param token token
     */
    public void setToken(String username, String token) {
        redisTemplate.opsForValue().set(getTokenKey(username, token), "1", Duration.ofDays(2));
    }

    /**
     * 清理指定用户的所有已注册token，操作将导致用户需要重新登录
     * @param username  用户名
     */
    public void cleanUserToken(String username) {
        redisTemplate.delete(redisDao.scanKeys(TOKEN_KEY + username + "::*"));
    }

    /**
     * 判断用户鉴权token是否有效
     * @param username token对应的用户名
     * @param token token
     * @return  token有效返回true，否则返回false
     */
    public boolean isTokenValid(String username, String token) {
        return redisTemplate.opsForValue().get(getTokenKey(username, token)) != null;
    }

}