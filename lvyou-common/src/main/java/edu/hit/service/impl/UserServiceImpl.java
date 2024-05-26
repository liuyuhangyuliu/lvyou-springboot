package edu.hit.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import edu.hit.utils.Response;
import edu.hit.utils.StatusCode;
import edu.hit.mapper.UserMapper;
import edu.hit.service.UserService;
import edu.hit.entity.User;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {

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



        if(insert >= 0){
            return new Response(StatusCode.OK.setMsg("创建新用户成功"),user);
        }else{
            return new Response(StatusCode.ERROR.set("A015","创建新用户失败"),null);
        }
    }

    @Override
    public User findByUserName(String username) {
        return userMapper.selectOne(new QueryWrapper<User>().eq("username",username));
    }


}




