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

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.access_expiration}")
    private Long access_expiration;
    @Value("${jwt.refresh_expiration}")
    private Long refresh_expiration;



    public String createToken(String username) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, username);
        claims.put(CLAIM_KEY_CREATED, new Date());
        return createToken(claims,access_expiration);
    }


    public String getUsernameFromToken(String token) {
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


    private Claims getClaimsFromToken(String token) {
        Claims claims = null;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            e.printStackTrace();
        } catch (UnsupportedJwtException e) {
            e.printStackTrace();
        } catch (MalformedJwtException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return claims;
    }



    private String createToken(Map<String, Object> claims,Long expiration) {

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.ES512, secret)
                .compact();
    }




    public boolean validateToken(String token, Subject subject) {

        String username = getUsernameFromToken(token);
        return username.equals(subject.getPrincipal()) && !isTokenExpired(token);
    }


    private boolean isTokenExpired(String token) {
        Date expiredDate = getExpiredDateFeomToken(token);
        return expiredDate.before(new Date());
    }


    private Date getExpiredDateFeomToken(String token) {
        Claims claims = getClaimsFromToken(token);
        return claims.getExpiration();
    }


    public boolean canBeRefreshed(String token){
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

