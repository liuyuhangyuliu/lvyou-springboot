package edu.hit.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.mail.MailUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import edu.hit.entity.UserBO;
import edu.hit.utils.JwtUtil;
import edu.hit.utils.RedisUtil;
import edu.hit.utils.Response;
import edu.hit.utils.StatusCode;
import edu.hit.mapper.UserMapper;
import edu.hit.service.UserService;
import edu.hit.entity.User;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;

import static edu.hit.utils.StatusCode.*;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

    @Value("${jwt.userBO_in_Redis_expiration}")
    private Long USERBO_IN_REDIS_EXPIRATION;

    @Value("${jwt.verify_code_expiration}")
    private Long verify_code_expiration;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Response register(User user) throws RuntimeException{

        if(findByUserName(user.getUsername()) != null){
            return new Response(StatusCode.ERROR.set("A014","用户名已存在"),null);
        }

        String ss = RandomUtil.randomString(6);
        user.setSalt(ss);
        Md5Hash md5Hash = new Md5Hash(user.getPassword(), ss, 1024);
        user.setPassword(md5Hash.toHex());


        int insert = 0;

        //小于0会抛异常吗

        insert = userMapper.insert(user);

        UserBO userBO = BeanUtil.copyProperties(user, UserBO.class);
        userBO.setAccessToken(JwtUtil.genAccessToken(user.getUsername()));

        if(insert >= 0){

            redisUtil.set(user.getUsername(), userBO,USERBO_IN_REDIS_EXPIRATION);

            return new Response(StatusCode.OK.setMsg("创建新用户成功"),userBO);
        }else{
            return new Response(StatusCode.ERROR.set("A015","创建新用户失败"),null);
        }
    }

    @Override
    public User findByUserName(String username) {
        return userMapper.selectOne(new QueryWrapper<User>().eq("username",username));
    }

    public User findByColumn(String column,String value){
        return userMapper.selectOne(new QueryWrapper<User>().eq(column,value));
    }


    @Override
    public Response sendVerifyCode(String mailAddress,String loginOrRegister){

        User user = findByColumn("email", mailAddress);
        if(user == null && loginOrRegister.equals("login")){
            return new Response(ERROR.set(ACCOUNT_NOT_FOUND_DURING_LOGIN,"account not found by email"),null);
        }
        if(user != null && loginOrRegister.equals("register")){
            return new Response(ERROR.set(ACCOUNT_EXISTS_DURING_REGISTER,"account exists during register(search with email)"),null);
        }


        String code = RandomUtil.randomString(4);
        String content = "您正在登录驴友辅助系统，验证码为【" + code + "】,有效时长3分钟";
        MailUtil.sendText(mailAddress,"【驴友辅助系统】",content);
        redisUtil.set(mailAddress,code,verify_code_expiration);
        return new Response(StatusCode.OK,null);
    }

    @Override
    public Response loginByMail(String mailAddress, String verifyCode) {

        String code = (String)redisUtil.get(mailAddress);

        if(code == null){
            return new Response(ERROR.set(VERIFY_CODE_EXPIRED,"verify code is expired"),null);
        }




        if(verifyCode.equals(code)){
            User user = findByColumn("email",mailAddress);
            UserBO userBO = BeanUtil.copyProperties(user, UserBO.class);
            userBO.setAccessToken(JwtUtil.genAccessToken(user.getUsername()));
            return new Response(OK,userBO);
        }else{
            return new Response(ERROR.set(INCORRECT_VERIFY_CODE,"incorrect verify code"),null);
        }



    }

    @Override
    public Response registerByMail(String mailAddress, String verifyCode, String username) {
        String code = (String)redisUtil.get(mailAddress);

        if(code == null){
            return new Response(ERROR.set(VERIFY_CODE_EXPIRED,"verify code is expired"),null);
        }




        if(verifyCode.equals(code)){

            return register(new User().setEmail(mailAddress));


        }else{
            return new Response(ERROR.set(INCORRECT_VERIFY_CODE,"incorrect verify code"),null);
        }


    }


}




