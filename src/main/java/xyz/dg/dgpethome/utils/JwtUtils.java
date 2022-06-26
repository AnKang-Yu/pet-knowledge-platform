package xyz.dg.dgpethome.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import xyz.dg.dgpethome.myexceptions.JsonException;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: dgpethome
 * @description:
 * @author: ruihao_ji
 * @create: 2022-06-13 10:48
 **/
public class JwtUtils {
    private final static int EXPIRATION_TIME = 60*60*24*31;
    private static String SECRET = "1145141919810";
    public static final String AUTHORIZATION = "Authorization";

    public static void setSecret(String secret) {
        JwtUtils.SECRET = secret;
    }

    /**
     * 生成一个包含了data作为负载信息的token
     * @param data  要附加的数据
     * @param expr  token有效时间，单位为秒
     * @return  token字符串
     */
    public static String generateToken(Object data, int expr) {
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        Date expiration = null;
        if (expr > 0) {
            calendar.setTime(now);
            calendar.add(Calendar.SECOND, expr);
            expiration = calendar.getTime();
        }

        Map<String, Object> map = new HashMap<>();
        map.put("data", data);
        return Jwts.builder().
                setClaims(map)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, SECRET)
                .compact();
    }

    /**
     * 生成一个包含了data作为负载信息的token，默认有效时间为一天
     * @param data  要附加的数据
     * @return  token字符串
     */
    public static String generateToken(Object data) {
        return generateToken(data, EXPIRATION_TIME);
    }

    /**
     * 解析一个token中的负载数据的json
     * @param token 输入的token
     * @return json字符串
     */
    public static String parse(String token) {
        try {
            Claims body = Jwts.parser()
                    .setSigningKey(SECRET)
                    .parseClaimsJws(token)
                    .getBody();
            return (String)body.get("data");
        } catch (ExpiredJwtException e) {
            throw new JsonException("token已过期");
        } catch (Exception e) {
            throw new JsonException("token无效");
        }
    }
}

