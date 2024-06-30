package edu.hit.lvyoubackend.controller;

import cn.hutool.json.JSONObject;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import edu.hit.lvyoubackend.entity.Application;
import edu.hit.lvyoubackend.entity.Join;
import edu.hit.lvyoubackend.entity.Schedule;
import edu.hit.lvyoubackend.entity.User;
import edu.hit.lvyoubackend.enums.ApplicationState;
import edu.hit.lvyoubackend.enums.JoinRole;
import edu.hit.lvyoubackend.enums.ScheduleState;
import edu.hit.lvyoubackend.service.ApplicationService;
import edu.hit.lvyoubackend.service.JoinService;
import edu.hit.lvyoubackend.service.ScheduleService;
import edu.hit.lvyoubackend.service.UserService;
import edu.hit.lvyoubackend.utils.Response;
import edu.hit.lvyoubackend.utils.StatusCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static edu.hit.lvyoubackend.utils.StatusCode.APPLY_FOR_CLOSED_SCHEDULE;
import static edu.hit.lvyoubackend.utils.StatusCode.APPLY_FOR_OWN_SCHEDULE;


@RestController
@Slf4j
@RequestMapping("/api/application")
@Tag(name = "申请接口")
public class ApplicationController {
    @Autowired
    private UserService userService;

    @Autowired
    private JoinService joinService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private ApplicationService applicationService;

    @PostMapping()
    @Parameters({@Parameter(name = "scheduleId",description = "行程id"),
                @Parameter(name = "uid",description = "用户id")})
    public Response submitApplication( @RequestBody Map<String,Integer> bodyMap,
                                      HttpServletRequest request){

        LocalDateTime now = LocalDateTime.now();
        Integer uid = bodyMap.get("uid");
        Integer scheduleId = bodyMap.get("scheduleId");

        Schedule schedule = scheduleService.getById(scheduleId);

        ScheduleState state = schedule.getState();
        if(state == ScheduleState.CLOSE){
            return new Response(StatusCode.ERROR.set(APPLY_FOR_CLOSED_SCHEDULE,"不能对已关闭的行程发出申请"),null);
        }
        if(Objects.equals(uid, schedule.getCreatedBy())){
            return new Response(StatusCode.ERROR.set(APPLY_FOR_OWN_SCHEDULE,"不能对自己创建的行程发出申请"),null);
        }

        String scheduleTitle = schedule.getTitle();

        log.info("uid:{}",uid);
        User user = userService.getById(uid);
        HashMap<String, String> map = new HashMap<>();
        map.put("username",user.getUsername());
        map.put("avatarSrc",user.getAvatarSrc());
        //String jsonStr = JSONUtil.toJsonStr(map);

        Application application = new Application(uid,scheduleId, now,new JSONObject(map),scheduleTitle);
        application.setState(ApplicationState.UNCHECK);
        if(applicationService.exists(new QueryWrapper<Application>().eq("uid",uid).eq("schedule_id",scheduleId))){
            applicationService.update(new UpdateWrapper<Application>().eq("uid",uid).eq("schedule_id",scheduleId).set("state",ApplicationState.UNCHECK));
        }else{
            applicationService.save(application);
        }

        return new Response(StatusCode.OK.set("000","提交申请成功"),application);
    }

    @Transactional
    @Operation(summary = "管理员同意加入行程申请")
    @PutMapping()
    @Parameter(name = "application",description = "申请json",in = ParameterIn.DEFAULT)
    public Response approveApplication(@RequestBody Application application){
        //log.debug(application.getScheduleTitle());


        applicationService.updateById(application);


        String msg = null;

        if(application.getState() == ApplicationState.APPROVE){
            msg = "同意申请成功";

            Join join = new Join(application.getUid(), application.getScheduleId(), JoinRole.USER, application.getScheduleTitle(), LocalDateTime.now());
            joinService.save(join);

            //TODO: current_number要检查 怎么设置约束或创建视图
            scheduleService.update(
                    new UpdateWrapper<Schedule>()
                            .eq("schedule_id", application.getScheduleId())
                            .setSql("current_number = current_number + 1"));

        }else if(application.getState() == ApplicationState.REJECT){
            msg = "拒绝申请成功";
        }

        return new Response(StatusCode.OK.set("000",msg),application);
    }


    @Operation(summary = "展示我需要审核的申请")
    @GetMapping()
    public Response showApplicationIShouldAudit(@RequestParam Integer uid){
        return applicationService.getApplicationIShouldAudit(uid);

    }




}
