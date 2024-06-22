package edu.hit.lvyoubackend.controller;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import edu.hit.lvyoubackend.entity.Application;
import edu.hit.lvyoubackend.entity.Join;
import edu.hit.lvyoubackend.mapper.ApplicationMapper;
import edu.hit.lvyoubackend.service.ApplicationService;
import edu.hit.lvyoubackend.service.JoinService;
import edu.hit.lvyoubackend.service.UserService;
import edu.hit.lvyoubackend.utils.Response;
import edu.hit.lvyoubackend.utils.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/notice")
public class NoticeController {


    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private ApplicationMapper applicationMapper;

    @Autowired
    private JoinService joinService;

    @Autowired
    private UserService userService;


    //FIXME: 不应该传uid
    @GetMapping()
    public Response showNotices(@RequestParam Integer uid, HttpServletRequest request) {
        //String username =(String) request.getAttribute("username");
        //Integer uid = userService.getOne(new QueryWrapper<User>().eq("username", username)).getUid().intValue();

        //作为管理员，我要审核的申请
        List<Application> application = applicationMapper.getApplicationIShouldAudit(uid);
        //我发送的申请通过或者被队伍的管理员邀请到行程中的通知
        List<Join> join = joinService.list(new QueryWrapper<Join>()
                .eq("uid", uid)
                .isNotNull("schedule_id"));
        //我被拒绝的申请
        List<Application> rejectList = applicationService.list(new QueryWrapper<Application>()
                .isNotNull("schedule_id")
                .eq("state", "REJECT")
                .eq("uid", uid));

        JSONArray array = new JSONArray();
        array.addAll(application);
        array.addAll(join);
        array.addAll(rejectList);

        array.sort(Comparator.comparing(obj -> ((JSONObject) obj).getDate("createdAt")).reversed());

        return new Response(StatusCode.OK.set("000", "获取待审核的申请和计入队伍通知"), array);
    }
}
