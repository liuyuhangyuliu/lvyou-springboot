package edu.hit.lvyouweb.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.hit.entity.User;
import edu.hit.utils.StatusCode;
import edu.hit.utils.Response;
import edu.hit.service.UserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.servlet.http.HttpSession;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/api/user")
public class LoginController {

    @Autowired
    private UserService userService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "000",description = "登陆成功"),
            @ApiResponse(responseCode = "A011",description = "未找到用户名或密码不匹配"),
            @ApiResponse(responseCode = "A012",description = "其他认证错误"),

    })
    @PostMapping("/login")
    //TODO 后端参数的校验
    public Response login(@RequestBody HashMap<String, String> params, HttpSession httpSession){
        String username = params.get("username");
        String password = params.get("password");

        User user;

        //FIXME:因该把逻辑写到Service层，解耦
        if(StringUtils.hasText(username) && StringUtils.hasText(password)){
            try{
                Subject subject = SecurityUtils.getSubject();
                //Session session = subject.getSession();
                UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
                subject.login(usernamePasswordToken);

                QueryWrapper<User> queryWrapper = new QueryWrapper<>();
                user = userService.getOne(queryWrapper.eq("username",username));

                //session.setAttribute("user",user);

                return new Response(StatusCode.OK.setMsg("登陆成功"),user);

            }
            //不应该分开提示，提高安全性
            catch(UnknownAccountException | IncorrectCredentialsException e){
                return new Response(StatusCode.ERROR.set("A011","用户名或密码错误！"),null);
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
}
