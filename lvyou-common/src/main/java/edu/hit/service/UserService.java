package edu.hit.service;


import com.baomidou.mybatisplus.extension.service.IService;
import edu.hit.entity.User;
import edu.hit.utils.Response;


public interface UserService extends IService<User> {

    Response register(User user) throws RuntimeException;

    User findByUserName(String username);

}
