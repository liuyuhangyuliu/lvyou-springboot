package edu.hit.service;


import com.baomidou.mybatisplus.extension.service.IService;
import edu.hit.entity.User;
import edu.hit.entity.UserBO;
import edu.hit.utils.Response;


public interface UserService extends IService<User> {

    Response register(User user) throws RuntimeException;

    User findByUserName(String username);

    UserBO getUserBOById(Integer id);

    public Response sendVerifyCode(String mailAddress,String loginOrRegister);

    Response loginByMail(String mailAddress,String code);

    public Response registerByMail(String mailAddress, String verifyCode, String username);

}
