package edu.hit.lvyouweb.controller;


import edu.hit.entity.Join;
import edu.hit.enums.JoinRole;
import edu.hit.entity.Schedule;
import edu.hit.service.JoinService;
import edu.hit.service.ScheduleService;
import edu.hit.service.UserService;
import edu.hit.utils.Response;
import edu.hit.utils.StatusCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@CrossOrigin(originPatterns = "http://localhost:3000")
@RestController
@Slf4j
@RequestMapping("/api/schedule")
@Tag(name ="行程接口")
public class ScheduleController {

    @Autowired
    private UserService userService;

    @Autowired
    private JoinService joinService;

    @Autowired
    private ScheduleService scheduleService;

    @Operation(summary = "展示行程（每次返回10条）")
    @GetMapping()
    public Response showSchedules(HttpServletRequest request, HttpServletResponse response){
        log.debug(request.getHeader("origin"));
        return scheduleService.getLimitedSchedules();

    }

    @Operation(summary = "展示行程详情")
    @GetMapping("/{scheduleId}")
    @Parameters(@Parameter(name = "sceduleId",description = "行程id",in = ParameterIn.PATH))
    public Response showScheduleDetail(@PathVariable  Integer scheduleId){
        return new Response(StatusCode.OK.set("000","获取行程详情成功"),scheduleService.getById(scheduleId));
    }

    @Operation(summary = "创建行程")
    @Parameters(@Parameter(name = "schedule",description = "行程json",in = ParameterIn.DEFAULT))
    @PostMapping()
    public Response createSchedule(@RequestBody  Schedule schedule, HttpServletRequest request){

        String username = (String) request.getAttribute("username");
        Integer uid = userService.findByUserName(username).getUid().intValue();

        LocalDateTime now = LocalDateTime.now();
        schedule.setCreatedBy(uid);
        schedule.setCreatedAt(now);
        scheduleService.save(schedule);

        Join join = new Join(uid,schedule.getScheduleId(), JoinRole.ADMIN,now);
        joinService.save(join);




        return new Response(StatusCode.OK.set("000","创建行程成功"),null);
    }

//    @GetMapping()
//    public Response showSchedulesIJoin(@RequestParam("uid") Integer uid){
////        joinService.list()
////        return new Response(StatusCode.OK,)
//        return null;
//    }


}
