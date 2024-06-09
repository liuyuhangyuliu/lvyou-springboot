package edu.hit.lvyouweb.controller;

import edu.hit.entity.Application;
import edu.hit.service.ApplicationService;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

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
    @Parameters({@Parameter(name = "scheduleId",description = "行程id",in = ParameterIn.QUERY),
                @Parameter(name = "uid",description = "用户id",in = ParameterIn.QUERY)})
    public Response submitApplication(@RequestParam("scheduleId") Integer scheduleId,
                                      @RequestParam("uid") Integer uid,
                                      HttpServletRequest request){

        LocalDateTime now = LocalDateTime.now();
        Application application = new Application(scheduleId, uid, now);
        applicationService.save(application);
        return new Response(StatusCode.OK,application);
    }

    @Operation(summary = "管理员同意加入行程申请")
    @PutMapping()
    @Parameter(name = "application",description = "申请json",in = ParameterIn.DEFAULT)
    public Response approveApplication(@RequestBody Application application){

        application.setApproval(true);
        applicationService.updateById(application);
        return new Response(StatusCode.OK,application);
    }




}
