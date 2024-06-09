package edu.hit.lvyouweb.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import edu.hit.entity.Join;
import edu.hit.enums.JoinRole;
import edu.hit.service.JoinService;
import edu.hit.utils.Response;
import edu.hit.utils.StatusCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
class JoinControllerTest {

    @Autowired
    private JoinService joinService;

    @Test
    public void enumTest(){
        //joinService.save(new Join(1,1, JoinRole.ADMIN, LocalDateTime.now()));
        Join join = joinService.getOne(new QueryWrapper<Join>().eq("uid",1));

        String jsonStr = JSONUtil.toJsonStr(new Response(StatusCode.OK,join));
        System.out.println(jsonStr);
    }

}