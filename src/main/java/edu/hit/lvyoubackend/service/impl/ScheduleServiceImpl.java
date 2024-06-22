package edu.hit.lvyoubackend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import edu.hit.lvyoubackend.entity.*;
import edu.hit.lvyoubackend.mapper.ScheduleMapper;
import edu.hit.lvyoubackend.service.ApplicationService;
import edu.hit.lvyoubackend.service.JoinService;
import edu.hit.lvyoubackend.service.ScheduleService;
import edu.hit.lvyoubackend.service.UserService;
import edu.hit.lvyoubackend.utils.Response;
import edu.hit.lvyoubackend.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author DELL
 * @description 针对表【schedule】的数据库操作Service实现
 * @createDate 2024-06-06 11:26:41
 */
@Slf4j
@Service
public class ScheduleServiceImpl extends ServiceImpl<ScheduleMapper, Schedule>
        implements ScheduleService {

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private JoinService joinService;

    @Autowired
    private ScheduleMapper scheduleMapper;


    @Override
    public Response getLimitedSchedules() {

        QueryWrapper<Schedule> wrapper = new QueryWrapper<>();
        wrapper.last("LIMIT 10");
        List<Schedule> list = list(wrapper);
//        Schedule schedule = list.get(0);
//        JSONObject jsonObject = JSONUtil.parseObj(schedule.getImages());
//        log.info((String)jsonObject.get("image1_url"));
        List<ScheduleVO> voList = new ArrayList<>();
        for (Schedule s : list) {
            ScheduleVO scheduleVO = BeanUtil.copyProperties(s, ScheduleVO.class);
            scheduleVO.setUserBO(userService.getUserBOById(s.getCreatedBy()));
            voList.add(scheduleVO);
        }
        return new Response(StatusCode.OK.set("000", "SUCCESS"), voList);
    }


    @Override
    public Object getScheduleDetails(Integer uid, Integer scheduleId) {
        Schedule schedule = getById(scheduleId);
        Map<String, Object> map = BeanUtil.beanToMap(schedule);
        QueryWrapper<Application> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid", uid).eq("schedule_id", scheduleId);
        //TODO 应保证uid*scheduleId为unique,用户在提交前应检查
        Application application = applicationService.getOne(queryWrapper);
        if (application != null)
            map.put("applicationState", application.getState());
        else {
            Join join = joinService.getOne(new QueryWrapper<Join>()
                    .eq("schedule_id", scheduleId)
                    .eq("uid", uid));
            if(join != null){
                map.put("applicationState","INVITED");
            }else {
                map.put("applicationState", "UNAPPLY");
            }

        }
//FIXME: 这里为什么没返回scheduleVO，而是把userBO放到map里了
        UserBO userBO = userService.getUserBOById(schedule.getCreatedBy());
        map.put("user", userBO);

        return map;
    }

    @Override
    public ScheduleVO getScheduleVObyId(Integer scheduleId) {
        Schedule schedule = getById(scheduleId);
        ScheduleVO scheduleVO = BeanUtil.copyProperties(schedule, ScheduleVO.class);
        scheduleVO.setUserBO(userService.getUserBOById(schedule.getCreatedBy()));
        return scheduleVO;
    }

    @Override
    public List<ScheduleVO> getScheduleByPage(Integer page, Integer size) {
        Page<Schedule> schedulePage = new Page<>(page, size);
        QueryWrapper<Schedule> scheduleQueryWrapper = new QueryWrapper<>();
        scheduleQueryWrapper.orderByDesc("created_at");

        scheduleMapper.selectPage(schedulePage, scheduleQueryWrapper);
        List<ScheduleVO> voList = new ArrayList<>();
        for (Schedule s : schedulePage.getRecords()) {
            ScheduleVO scheduleVO = BeanUtil.copyProperties(s, ScheduleVO.class);
            scheduleVO.setUserBO(userService.getUserBOById(s.getCreatedBy()));
            voList.add(scheduleVO);
        }
        return voList;
    }
}




