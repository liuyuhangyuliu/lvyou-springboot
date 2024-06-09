package edu.hit.lvyouweb.controller;

import edu.hit.service.JoinService;
import edu.hit.service.ScheduleService;
import edu.hit.service.UserService;
import edu.hit.utils.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/api/join")
@Tag(name = "参加记录接口",description = "")
public class JoinController {
    @Autowired
    private UserService userService;

    @Autowired
    private JoinService joinService;

    @Autowired
    private ScheduleService scheduleService;

    @Operation(summary = "join in the schedule(only when invited)")
    @PostMapping()
    public Response joinSchedule(){
        return null;
    }
}
