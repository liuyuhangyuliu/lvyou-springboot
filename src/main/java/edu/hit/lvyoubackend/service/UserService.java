package edu.hit.lvyoubackend.service;


import com.baomidou.mybatisplus.extension.service.IService;
import edu.hit.lvyoubackend.entity.User;
import edu.hit.lvyoubackend.entity.UserBO;
import edu.hit.lvyoubackend.utils.Response;


public interface UserService extends IService<User> {

    Response register(User user) throws RuntimeException;

    User findByUserName(String username);

    UserBO getUserBOById(Integer id);

    UserBO getUserBOByUsername(String username);

    public Response sendVerifyCode(String mailAddress,String loginOrRegister);

    Response loginByMail(String mailAddress,String code);

    public Response registerByMail(String mailAddress, String verifyCode, String username);

    void sendMail(String content,String ... mailAddress);

    void hello();

}
