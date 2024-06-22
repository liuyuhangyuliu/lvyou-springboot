package edu.hit.lvyoubackend.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import edu.hit.lvyoubackend.entity.Join;
import edu.hit.lvyoubackend.entity.Member;
import edu.hit.lvyoubackend.entity.Schedule;
import edu.hit.lvyoubackend.entity.ScheduleVO;

import edu.hit.lvyoubackend.enums.JoinRole;
import edu.hit.lvyoubackend.service.JoinService;
import edu.hit.lvyoubackend.service.MemberService;
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
import org.springframework.web.bind.annotation.*;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@CrossOrigin(originPatterns = "http://localhost:3000")
@RestController
@Slf4j
@RequestMapping("/api/schedule")
@Tag(name = "行程接口")
public class ScheduleController {

    @Autowired
    private UserService userService;

    @Autowired
    private JoinService joinService;

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private MemberService memberService;

    @Operation(summary = "展示行程（每次返回 条）")
    @GetMapping()
    public Response showSchedules(HttpServletRequest request, @RequestParam Integer page) {
        log.debug(request.getHeader("origin"));
        List<ScheduleVO> scheduleByPage = scheduleService.getScheduleByPage(page, 2);
        return new Response(StatusCode.OK.set("000","获得行程"),scheduleByPage);

    }

    @Operation(summary = "展示行程详情，详情页面")
    @GetMapping("/{scheduleId}")
    @Parameters(@Parameter(name = "sceduleId", description = "行程id", in = ParameterIn.PATH))
    public Response showScheduleDetail(@PathVariable Integer scheduleId, @RequestParam Integer uid) {
        //String username = (String)request.getAttribute("username");
        return new Response(StatusCode.OK.set("000", "获取行程详情成功"), scheduleService.getScheduleDetails(uid, scheduleId));
    }

    @Operation(summary = "修改行程信息页面，返回schedule")
    @GetMapping("/scheduleInfo/{scheduleId}")
    public Response showSchedule(@PathVariable Integer scheduleId) {
        return new Response(StatusCode.OK.set("000", "获取行程信息"), scheduleService.getById(scheduleId));
    }

    @Operation(summary = "创建行程")
    @Parameters(@Parameter(name = "schedule", description = "行程json", in = ParameterIn.DEFAULT))
    @PostMapping()
    public Response createSchedule(@RequestBody Schedule schedule, HttpServletRequest request) {

        String username = (String) request.getAttribute("username");
        Integer uid = userService.findByUserName(username).getUid().intValue();

        LocalDateTime now = LocalDateTime.now();
        schedule.setCreatedBy(uid);
        schedule.setCreatedAt(now);
        if (schedule.getDescription() == null) {
            schedule.setDescription(schedule.getTitle());
        }
        if (schedule.getDetails() == null) {
            schedule.setDetails(schedule.getTitle());
        }
        scheduleService.save(schedule);

        Join join = new Join(uid, schedule.getScheduleId(), JoinRole.OWNER, schedule.getTitle(), now);
        joinService.save(join);


        return new Response(StatusCode.OK.set("000", "创建行程成功"), null);
    }

    @Operation(summary = "返回该用户管理和参加的行程列表")
    @GetMapping("/mySchedule/{uid}")
    public Response showSchedulesIJoin(@PathVariable Integer uid) {
//        joinService.list()
//        return new Response(StatusCode.OK,)
        QueryWrapper<Join> wrapper = new QueryWrapper<>();
        ArrayList<ScheduleVO> adminScheduleList = new ArrayList<>(), userScheduleList = new ArrayList<>();

        List<Join> adminJoinList = joinService.list(
                wrapper.eq("uid", uid)
                        .and(joinQueryWrapper -> {
                            joinQueryWrapper.eq("role", "ADMIN").or().eq("role", "OWNER");
                        })
        );

        adminJoinList.forEach(join -> {
            Integer scheduleId = join.getScheduleId();
            ScheduleVO scheduleVO = scheduleService.getScheduleVObyId(scheduleId);
            adminScheduleList.add(scheduleVO);
        });

        wrapper.clear();
        List<Join> userJoinList = joinService.list(wrapper.eq("role", "USER").eq("uid", uid));


        userJoinList.forEach(join -> {
            Integer scheduleId = join.getScheduleId();
            ScheduleVO scheduleVO = scheduleService.getScheduleVObyId(scheduleId);
            userScheduleList.add(scheduleVO);
        });

        HashMap<String, List<ScheduleVO>> map = new HashMap<>();
        map.put("admin", adminScheduleList);
        map.put("user", userScheduleList);

        return new Response(StatusCode.OK.set("000", "返回该用户参加和管理的行程"), map);
    }

    @Operation(summary = "更新行程信息")
    @PutMapping()
    public Response updateSchedule(@RequestBody Schedule schedule) {
        scheduleService.updateById(schedule);
        return new Response(StatusCode.OK.set("000", "修改行程信息"), schedule);
    }

    @Operation(summary = "获取该行程的所有成员")
    @GetMapping("/member/{scheduleId}")
    public Response showMembers(@PathVariable Integer scheduleId) {
        return new Response(StatusCode.OK.set("000", "获取行程所有成员"), memberService.list(new QueryWrapper<Member>().eq("schedule_id", scheduleId)));
    }


}
