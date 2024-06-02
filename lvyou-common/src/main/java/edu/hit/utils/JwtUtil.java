package edu.hit.utils;


import io.jsonwebtoken.*;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//注册组件
@Component
@Data
@Slf4j
public class JwtUtil {

    private static final String CLAIM_KEY_USERNAME = "username";
    private static final String CLAIM_KEY_CREATED = "created";


    private static String secret;
    private static Long access_expiration;
    private static Long refresh_expiration;

    public JwtUtil(@Value("${jwt.secret}")String secret1,
                   @Value("${jwt.access_expiration}")Long access_expiration1,
                   @Value("${jwt.refresh_expiration}")Long refresh_expiration1){
        this.secret = secret1;
        this.access_expiration = access_expiration1;
        this.refresh_expiration = refresh_expiration1;
    }



    public static String createToken(String username) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, username);
        claims.put(CLAIM_KEY_CREATED, new Date());
        return createToken(claims,access_expiration);
    }


    public static String getUsernameFromToken(String token) {
        String username = "";
        try {
            Claims claims = getClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
            log.info("error:{}", "用户名未能获取 from token");
        }
        return username;
    }


    private static Claims getClaimsFromToken(String token) {
        Claims claims = null;

        claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();

        return claims;
    }



    private static String createToken(Map<String, Object> claims, Long expiration) {

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.ES512, secret)
                .compact();
    }




    public static boolean validateToken(String token, Subject subject) {

        String username = getUsernameFromToken(token);
        return username.equals(subject.getPrincipal()) && !isTokenExpired(token);
    }


    private static boolean isTokenExpired(String token) {
        Date expiredDate = getExpiredDateFromToken(token);
        return expiredDate.before(new Date());
    }


    private static Date getExpiredDateFromToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }


    public static boolean canBeRefreshed(String token){
        return !isTokenExpired(token);
    }

//    //刷新token
//    public String refreshToken(String token){
//        Claims claims = getClaimsFromToken(token);
//        //修改为当前时间
//        claims.put(CLAIM_KEY_CREATED,new Date());
//        return createToken(claims);
//    }

}

