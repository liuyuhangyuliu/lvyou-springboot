package edu.hit.lvyouweb.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.hit.entity.User;
import edu.hit.entity.UserBO;
import edu.hit.mapper.UserMapper;
import edu.hit.service.UserService;
import edu.hit.utils.StatusCode;
import edu.hit.utils.token.MailLoginToken;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
@ActiveProfiles("test")
class LoginControllerTest {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    private User user = new User();

    @BeforeEach
    void setUp() {
        user.setPassword("aaa").setUsername("aaa");
    }

    @Test
    public void insertTest(){
        userMapper.insert(user);
    }

    @Test
    public void beanCopyTest(){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        user = userService.getOne(queryWrapper.eq("username","admin"));
        UserBO userBO = BeanUtil.copyProperties(user, UserBO.class,"accessToken");
        //System.out.println(userBO.getUsername());
        userBO.setAccessToken("aaa");
        System.out.println(JSONUtil.toJsonStr(userBO));
    }

    @Test
    public void sendMailTest(){
        userService.sendVerifyCode("2196933343@qq.com","login");
    }

    @Test
    public void splitTest(){
        String path = "/login/verifyCode/{mailAddress}";
        System.out.println(path.split("/")[1]);
    }

    @Test
    public void sendMail(){
        MailAccount account = new MailAccount();
        account.setHost("smtp.qq.com");
        account.setPort(25);
        account.setAuth(true);
        account.setFrom("2196933343@qq.com");
        account.setUser("2196933343@qq.com");
        account.setPass("ydzmtksdtmjeeaac"); //密码
        MailUtil.send(account, CollUtil.newArrayList("2196933343@qq.com"), "测试", "邮件来自Hutool测试", false);
    }

    @Test
    public void enumTest(){
        StatusCode.OK.setMsg("msg");
        System.out.println(StatusCode.OK.getCode());
    }

    @AfterEach
    void tearDown() {
    }
}