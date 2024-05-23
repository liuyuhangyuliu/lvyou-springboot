package edu.hit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import edu.hit.mapper.UserMapper;
import edu.hit.service.UserService;
import edu.hit.entity.User;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService {



}




