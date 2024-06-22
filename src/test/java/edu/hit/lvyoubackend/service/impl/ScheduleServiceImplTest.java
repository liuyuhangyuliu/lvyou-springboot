package edu.hit.lvyoubackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import edu.hit.lvyoubackend.entity.Schedule;
import edu.hit.lvyoubackend.mapper.ScheduleMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
class ScheduleServiceImplTest {

    @Autowired
    private ScheduleMapper scheduleMapper;

    @Test
    void pageTest(){
        Page<Schedule> schedulePage = new Page<>(1, 1);
        QueryWrapper<Schedule> scheduleQueryWrapper = new QueryWrapper<>();
        scheduleQueryWrapper.orderByDesc("created_at");

        Page<Schedule> page1 = scheduleMapper.selectPage(schedulePage, scheduleQueryWrapper);
        schedulePage.getRecords().forEach(System.out::println);
    }
}