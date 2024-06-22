package edu.hit.lvyoubackend.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import edu.hit.lvyoubackend.entity.User;
import edu.hit.lvyoubackend.entity.UserBO;
import edu.hit.lvyoubackend.service.UserService;
import edu.hit.lvyoubackend.utils.JwtUtil;
import edu.hit.lvyoubackend.utils.RedisUtil;
import edu.hit.lvyoubackend.utils.Response;
import edu.hit.lvyoubackend.utils.StatusCode;
import edu.hit.lvyoubackend.utils.token.MailLoginToken;
import edu.hit.lvyoubackend.utils.token.MailRegisterToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
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

import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;

import static edu.hit.lvyoubackend.utils.StatusCode.*;

@Slf4j
@CrossOrigin(originPatterns = "http://localhost:3000")
@RestController
@RequestMapping("/api/user")
@Tag(name ="登录注册接口")
public class LoginController {

    @Value("${jwt.userBO_in_Redis_expiration}")
    private Long USERBO_IN_REDIS_EXPIRATION;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisUtil redisUtil;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "000",description = "登录成功"),
            @ApiResponse(responseCode = "A011",description = "未找到用户名或密码不匹配"),
            @ApiResponse(responseCode = "A012",description = "其他认证错误"),

    })
    @PostMapping("/login")
    //TODO 后端参数的校验
    @Operation(summary = "用户名登录")
    @Parameters(@Parameter(name = "params",description = "用户名密码json",in = ParameterIn.DEFAULT))
    public Response login(@RequestBody  HashMap<String, String> params){
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


                return new Response(StatusCode.OK.set("000","登陆成功"),userBO);

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
    @Operation(summary = "用户名密码注册")
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
    @Operation(summary = "邮箱验证码登录")
    @Parameters(@Parameter(name = "mailLoginToken",description = "邮箱验证码json",in = ParameterIn.DEFAULT))
    public Response loginByMail(@RequestBody @Validated MailLoginToken mailLoginToken) {
        String mailAddress = mailLoginToken.getMailAddress();
        String code = mailLoginToken.getCode();
        log.info("mailAddress:{}",mailAddress);
        log.info("code:{}",code);

        return userService.loginByMail(mailAddress,code);

    }

    @Operation(summary = "用户名邮箱验证码登录")
    @PostMapping("/registerByMail")
    public Response registerByMail(@RequestBody @Valid MailRegisterToken mailRegisterToken){
        String mailAddress = mailRegisterToken.getMailAddress();
        String username = mailRegisterToken.getUsername();
        String code = mailRegisterToken.getCode();

        return userService.registerByMail(mailAddress,username,code);


    }


    //TODO 发邮件速度慢，考虑异步
    @Operation(summary = "发送验证码")
    @GetMapping({"/login/verifyCode/{mailAddress}","/register/verifyCode/{mailAddress}"})
    @Parameters(@Parameter(name = "mailAddress",description = "邮箱地址",in = ParameterIn.PATH))
    public Response sendVerifyCode(@PathVariable String mailAddress, HttpServletRequest request){
        String servletPath = request.getServletPath();
        //System.out.println(servletPath);
        String loginOrRegister = servletPath.split("/")[1];
        return userService.sendVerifyCode(mailAddress,loginOrRegister);
    }

    @Operation(summary = "退出登录")
    @GetMapping("/logout")
    public Response logout(HttpServletRequest request){
        String username = (String)request.getAttribute("username");
        redisUtil.remove(username);
        Subject subject = SecurityUtils.getSubject();
        if(subject != null){
            subject.logout();
        }
        return new Response(StatusCode.OK.set("000","成功退出登录"),null);
    }

    @Operation(summary = "测试")
    @GetMapping("/test/aaa/bbb")
    public Response test1(HttpServletRequest request){
        log.info(request.getContextPath());
        userService.hello();
        return new Response(OK,null);
    }

    @GetMapping("/test")
    public Response test(HttpServletRequest request){
        log.info("get username:{}",(String)request.getAttribute("username"));

        return new Response(OK,null);
    }
}
