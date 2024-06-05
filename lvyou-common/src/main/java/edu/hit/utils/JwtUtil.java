package edu.hit.utils;


import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * The type Jwt util.
 */
//注册组件
@Data
@Slf4j
@Component
public class JwtUtil {

    private static final String CLAIM_KEY_USERNAME = "username";
    private static final String CLAIM_KEY_CREATED = "created";
    private static final String JWT_ISS = "liuyuhang";



    private static String secret ;
    private static Long access_expiration ;
    private static Long refresh_expiration;

    JwtUtil(       @Value("${jwt.secret}")String secret1,
                   @Value("${jwt.access_expiration}")Long access_expiration1,
                   @Value("${jwt.refresh_expiration}")Long refresh_expiration1){
        secret = secret1;
        access_expiration = access_expiration1;
        refresh_expiration = refresh_expiration1;
    }


    //private static SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes());
/**
* secretKey不能提出来字段，secret是Value注入的，时机是什么，反正这里初始化secretKey时secret一定是null，
 *     抛异常，最后导致error creating bean
 *     这是最上边的异常，我一直被这个吸引注意力
 *     实际上应该关注最下面的 看caused by cant invoke getBytes 因为nullpointerexception
 */
    public static String genAccessToken(String username) {
        // 令牌id
        String uuid = UUID.randomUUID().toString();
        Date exprireDate = Date.from(Instant.now().plusSeconds(access_expiration));

        return Jwts.builder()
                // 设置头部信息header
                .header()
                .add("typ", "JWT")
                .add("alg", "HS256")
                .and()
                // 设置自定义负载信息payload
                .claim("username", username)
                // 令牌ID
                .id(uuid)
                // 过期日期
                .expiration(exprireDate)
                // 签发时间
                .issuedAt(new Date())
                // 签发者
                .issuer(JWT_ISS)
                // 签名
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()), Jwts.SIG.HS256)
                .compact();
    }
    /**
     * 解析token
     * @param token token
     * @return Jws<Claims>
     */
    public static Jws<Claims> parseClaim(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseSignedClaims(token);
    }

    public static JwsHeader parseHeader(String token) {
        return parseClaim(token).getHeader();
    }

    public static Claims parsePayload(String token) {
        return parseClaim(token).getPayload();
    }

}

