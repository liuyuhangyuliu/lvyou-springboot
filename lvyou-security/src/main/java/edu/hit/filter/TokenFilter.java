package edu.hit.filter;

import cn.hutool.json.JSONUtil;
import edu.hit.entity.User;
import edu.hit.entity.UserBO;
import edu.hit.utils.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

import static edu.hit.utils.StatusCode.*;


@Slf4j
@WebFilter(urlPatterns = {"/api/user/test","/api/schedule"})
public class TokenFilter extends OncePerRequestFilter {

    @Autowired
    private RedisUtil redisUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String token = request.getHeader("Authorization");
        Subject subject = SecurityUtils.getSubject();
        //log.info("subject:{}",subject.getPrincipal());

        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");


        response.setContentType("application/json;charset=UTF-8");

        //System.out.println(request.getMethod());

        //放行预检请求，大坑中的大坑
        //而且不知道为什么判断条件写request.getMethod() == "OPTIONS"就不行
        String method = request.getMethod();
        if(method.equals("OPTIONS")){

            return ;
        }

        if(token == null ){
            response.getWriter().write(
                    JSONUtil.toJsonStr(new Response(
                            StatusCode.OK.set("a","no token"),null)));
            //filterChain.doFilter(request,response);

            return;
        }else{
            try{
                String username = "";
                username = (String) JwtUtil.parsePayload(token).get("username");
                log.info("username:{}",username);
                request.setAttribute("username",username);
                filterChain.doFilter(request,response);
            }catch (ExpiredJwtException expiredJwtException){

                Claims claims = expiredJwtException.getClaims();
                String username = (String) claims.get("username");
                log.info("username:{}",username);
                UserBO user = (UserBO)redisUtil.get(username);

                if(user != null){
                    String accessToken = JwtUtil.genAccessToken(username);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write(
                            JSONUtil.toJsonStr(new Response(
                                    StatusCode.OK.set(REFRESH_ACCESS_TOKEN,"access token is expired and return new access token"),accessToken)));
                    return;
                }else {

                    subject.logout();

                    response.getWriter().write(
                            JSONUtil.toJsonStr(new Response(
                                    StatusCode.ERROR.set(EXPIRED_ACCESS_TOKEN,"access token is expired and user details in cache are expired"),null)));
                    return;
                }

            } catch (MalformedJwtException | SignatureException | IllegalArgumentException e) {

                filterChain.doFilter(request,response);
                return;
            } catch (Exception e) {
                filterChain.doFilter(request,response);
                return;

            }

            //filterChain.doFilter(request,response);
            return;
// FIXME: subject为null

//            if(!username.equals(subject.getPrincipal())){
//
//                response.getWriter().write(
//                        JSONUtil.toJsonStr(new Response(
//                                StatusCode.ERROR.set(UNRECONGNIZED_PAYLOAD_USERNAME,"username in payload and principle in subject are inconsistent"),null)));
//                return;
//            }else {
//
//            }


        }
    }
}
