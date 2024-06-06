package edu.hit.lvyouweb.controller;


import edu.hit.entity.Join;
import edu.hit.entity.JoinRole;
import edu.hit.entity.Schedule;
import edu.hit.service.JoinService;
import edu.hit.service.ScheduleService;
import edu.hit.service.UserService;
import edu.hit.utils.Response;
import edu.hit.utils.StatusCode;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@Slf4j
@RequestMapping("/api/schedule")
public class ScheduleController {

    @Autowired
    private UserService userService;

    @Autowired
    private JoinService joinService;

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping()
    public Response showSchedules(){
        return scheduleService.getLimitedSchedules();

    }

    @GetMapping("/{scheduleId}")
    public Response showScheduleDetail(@PathVariable Integer scheduleId){
        return new Response(StatusCode.OK,scheduleService.getById(scheduleId));
    }

    @PostMapping()
    public Response createSchedule(@RequestBody Schedule schedule, HttpServletRequest request){

        String username = (String) request.getAttribute("username");
        Integer uid = userService.findByUserName(username).getUid().intValue();

        LocalDateTime now = LocalDateTime.now();

        Join join = new Join(uid,schedule.getScheduleId(), JoinRole.ADMIN.name(),now);


        joinService.save(join);


        schedule.setCreatedAt(now);

        scheduleService.save(schedule);

        return new Response(StatusCode.OK,null);
    }
}
