package edu.hit.lvyoubackend.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import edu.hit.lvyoubackend.entity.User;
import edu.hit.lvyoubackend.entity.UserBO;
import edu.hit.lvyoubackend.service.UserService;
import edu.hit.lvyoubackend.utils.Response;
import edu.hit.lvyoubackend.utils.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/profile/{username}")
    public Response getProfile(@PathVariable String username){
        return new Response(StatusCode.OK.set("000","获得userBO"),userService.getUserBOByUsername(username));
    }
    @GetMapping("/query/{query}")
    public Response getUsersByQuery(@PathVariable String query){
        List<User> list = userService.list(new QueryWrapper<User>()
                .select("DISTINCT uid,username,email")
                .like("username", "%" + query + "%")
                .or().like("email", "%" + query + "%")
        );
        ArrayList<UserBO> list1 = new ArrayList<>();
        for (User user : list) {
            UserBO userBO = BeanUtil.copyProperties(user, UserBO.class);
            list1.add(userBO);
        }
        return new Response(StatusCode.OK.set("000","返回查询结果"),list1);
    }
}
