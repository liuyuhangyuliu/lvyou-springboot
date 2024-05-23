package edu.hit.lvyouweb.controller;



import edu.hit.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
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

    @PostMapping("/login")
    public String login(@RequestBody HashMap<String, String> params, HttpSession session){
        String username = params.get("username");
        String password = params.get("password");
        //System.out.println(password);

        if(StringUtils.hasText(username) && StringUtils.hasText(password)){
            try{
                Subject subject = SecurityUtils.getSubject();
                UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password);
                subject.login(usernamePasswordToken);

            }
            catch (Exception e){
                e.printStackTrace();
                return e.getMessage();
            }
        }
        //userService.getOne()

        return "login successfully";

    }
}
