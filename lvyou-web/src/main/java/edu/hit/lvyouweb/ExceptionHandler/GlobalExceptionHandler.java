package edu.hit.lvyouweb.ExceptionHandler;


import edu.hit.utils.MyException;
import edu.hit.utils.Response;
import edu.hit.utils.StatusCode;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 一般自定义CustomException类，controller和interceptor里catch(AException|BException)
     * 然后throw到这里取出异常信息。
     * @param e
     * @return
     */

    @ExceptionHandler(MyException.class)
    public Response myExceptionHandler(MyException e){
        log.info(e.getMsg());
        return new Response(StatusCode.ERROR.set(e.getCode(), e.getMsg()), null);
    }

    @ExceptionHandler(JwtException.class)
   public Response jwtExceptionHandler(JwtException e){
        log.error(e.getMessage());
        if(e instanceof ExpiredJwtException){

        }else if (e instanceof UnsupportedJwtException){

        }else if(e instanceof SignatureException){

        }
        return new Response(StatusCode.ERROR.set("A",e.getMessage()),null);
   }
}
