package edu.hit.lvyouweb.controller;



import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.hit.utils.*;
import edu.hit.entity.User;
import edu.hit.entity.UserBO;
import edu.hit.service.UserService;
import edu.hit.utils.token.MailLoginToken;
import edu.hit.utils.token.MailRegisterToken;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

import static edu.hit.utils.StatusCode.*;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class LoginController {

    @Value("${jwt.userBO_in_Redis_expiration}")
    private Long USERBO_IN_REDIS_EXPIRATION;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisUtil redisUtil;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "000",description = "登陆成功"),
            @ApiResponse(responseCode = "A011",description = "未找到用户名或密码不匹配"),
            @ApiResponse(responseCode = "A012",description = "其他认证错误"),

    })
    @PostMapping("/login")
    //TODO 后端参数的校验
    public Response login(@RequestBody HashMap<String, String> params){
        String username = params.get("username");
        String password = params.get("password");

        User user;

        //FIXME:应该把逻辑写到Service层，解耦
        if(StringUtils.hasText(username) && StringUtils.hasText(password)){
            try{
                Subject subject = SecurityUtils.getSubject();


                UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
                subject.login(usernamePasswordToken);

                log.info("subject:{}",subject.getPrincipal());

                QueryWrapper<User> queryWrapper = new QueryWrapper<>();
                user = userService.getOne(queryWrapper.eq("username",username));
                UserBO userBO = BeanUtil.copyProperties(user, UserBO.class);
                userBO.setAccessToken(JwtUtil.genAccessToken(username));

                redisUtil.set(username,userBO,USERBO_IN_REDIS_EXPIRATION);


                return new Response(StatusCode.OK.setMsg("登陆成功"),userBO);

            }
            //不应该分开提示，提高安全性
            catch(UnknownAccountException | IncorrectCredentialsException e){
                return new Response(StatusCode.ERROR.set(WRONG_USERNAME_PWD,"用户名或密码错误！"),null);
            }catch (AuthenticationException ae){
                return new Response(StatusCode.ERROR.set("A012",ae.getMessage()),null);
            }
        }//参数检查，应该改变写法
        else{
            return new Response(StatusCode.ERROR.set("A013","用户名或密码为空"),null);
        }




    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "000",description = "注册成功"),
            @ApiResponse(responseCode = "A013",description = "用户名或密码为空"),
            @ApiResponse(responseCode = "A014",description = "用户名已存在"),
            @ApiResponse(responseCode = "A015",description = "注册新用户失败")
    })
    @PostMapping("/register")
    public Response register(@RequestBody HashMap<String, String> params){
        String username = params.get("username");
        String password = params.get("password");

        try{
            return userService.register(new User().setUsername(username).setPassword(password));
        }catch(RuntimeException e){
            return new Response(StatusCode.ERROR.set("A015",e.getMessage()),null);
        }

    }

    @PostMapping("/loginByMail")
    public Response loginByMail(@RequestBody @Validated MailLoginToken mailLoginToken) {
        String mailAddress = mailLoginToken.getMailAddress();
        String code = mailLoginToken.getCode();
        log.info("mailAddress:{}",mailAddress);
        log.info("code:{}",code);

        return userService.loginByMail(mailAddress,code);

    }

    @PostMapping("/registerByMail")
    public Response registerByMail(@RequestBody @Valid MailRegisterToken mailRegisterToken){
        String mailAddress = mailRegisterToken.getMailAddress();
        String username = mailRegisterToken.getUsername();
        String code = mailRegisterToken.getCode();

        return userService.registerByMail(mailAddress,username,code);


    }


    //TODO 发邮件速度慢，考虑异步
    @GetMapping({"/login/verifyCode/{mailAddress}","/register/verifyCode/{mailAddress}"})
    public Response sendVerifyCode(@PathVariable String mailAddress, HttpServletRequest request){
        String servletPath = request.getServletPath();
        String loginOrRegister = servletPath.split("//")[1];
        return userService.sendVerifyCode(mailAddress,loginOrRegister);
    }

    @GetMapping("/test/aaa/bbb")
    public Response test1(HttpServletRequest request){
        log.info(request.getContextPath());
        return new Response(OK,null);
    }

    @GetMapping("/test")
    public Response test(HttpServletRequest request){
        log.info("get username:{}",(String)request.getAttribute("username"));

        return new Response(OK,null);
    }
}
