package edu.hit.lvyoubackend.ExceptionHandler;



import edu.hit.lvyoubackend.utils.MyException;
import edu.hit.lvyoubackend.utils.Response;
import edu.hit.lvyoubackend.utils.StatusCode;
import io.jsonwebtoken.*;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.sql.SQLException;

import static edu.hit.lvyoubackend.utils.StatusCode.*;

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

    @ExceptionHandler(SQLException.class)
    public Response sqlExceptionHandler(SQLException e){
        log.error(e.getMessage());
        return new Response(ERROR.set(SQLEXCEPTION,e.getMessage()),null);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
     public Response methodArgumentNotValidException(MethodArgumentNotValidException e){
        log.debug(e.getMessage());
        return new Response(ERROR.setMsg(e.getMessage()),null);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Response ConstraintViolationExceptionHandler(ConstraintViolationException e){
        log.debug(e.getMessage());
        return new Response(ERROR.set(TABLE_FIELD_IS_EMPTY,"字段为空"),null);
    }

    @ExceptionHandler(JwtException.class)
    public Response jwtExceptionHandler(JwtException e){
        String message = e.getMessage();
        log.error(message);

        StatusCode error = StatusCode.ERROR;
        if(e instanceof ExpiredJwtException){
            error = error.set(EXPIRED_ACCESS_TOKEN,message);
        }else if (e instanceof UnsupportedJwtException){
            error = error.set(UNSUPPORTED_TOKEN,message);
        }else if(e instanceof SignatureException){
            error = error.set(BAD_SIGNATURE_TOKEN,message);
        }
        return new Response(error,null);
   }
}
