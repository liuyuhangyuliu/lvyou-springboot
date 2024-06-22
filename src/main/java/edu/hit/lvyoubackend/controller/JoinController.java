package edu.hit.lvyoubackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;

import edu.hit.lvyoubackend.entity.Join;
import edu.hit.lvyoubackend.enums.JoinRole;
import edu.hit.lvyoubackend.service.JoinService;
import edu.hit.lvyoubackend.service.ScheduleService;
import edu.hit.lvyoubackend.service.UserService;
import edu.hit.lvyoubackend.utils.Response;
import edu.hit.lvyoubackend.utils.StatusCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import edu.hit.lvyoubackend.enums.*;

import java.time.LocalDateTime;
import java.util.HashMap;

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

    @Operation(summary = "管理员将用户加入到队伍中")
    @PostMapping()
    public Response joinSchedule(@RequestBody HashMap<String,String> map){
        String username = map.get("username");
        Integer scheduleId = Integer.parseInt(map.get("scheduleId"));

        Integer uid = userService.getUserBOByUsername(username).getUid().intValue();
        String title = scheduleService.getScheduleVObyId(scheduleId).getTitle();
        joinService.save(new Join(uid,scheduleId, JoinRole.USER,title, LocalDateTime.now()));
        return new Response(StatusCode.OK.set("000","加入成员成功"),null);
    }

    @Operation(summary = "更改权限")
    @PutMapping("/member")
    public Response alterMemberRole(@RequestParam String username,@RequestParam Integer scheduleId){
        Integer uid = userService.getUserBOByUsername(username).getUid().intValue();
        joinService.update(new UpdateWrapper<Join>().eq("uid",uid).eq("schedule_id",scheduleId).set("role","ADMIN"));
        return new Response(StatusCode.OK.set("000","将用户设置为管理员"),null);
    }

    @Operation(summary = "移除成员")
    @DeleteMapping("/member")
    public Response removeMember(@RequestParam String username,@RequestParam Integer scheduleId){
        Integer uid = userService.getUserBOByUsername(username).getUid().intValue();
        joinService.remove(new QueryWrapper<Join>().eq("uid",uid).eq("schedule_id",scheduleId));
        return new Response(StatusCode.OK.set("000","将成员从行程中移除"),null);
    }
}
