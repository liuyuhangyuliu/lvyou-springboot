package edu.hit.lvyouweb.controller;

import edu.hit.entity.Schedule;
import edu.hit.service.ScheduleService;
import edu.hit.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
class ScheduleControllerTest {


    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private UserService userService;

    @Test
    public void userServiceTest(){
        System.out.println(userService.getById("1"));
    }

    @Test
    public void scheduleServiceTest(){

        System.out.println(scheduleService.getById("1"));
    }

    @Test
    public void autoIncrTest(){
        Schedule schedule = new Schedule();
        //schedule.setScheduleId(0);
        schedule.setTitle("111");
        scheduleService.save(schedule);
        /**
        * 不需要set设为自增的字段，save之后会把自增的id填好，前提是注解修饰IdType.AUTO
         */
        System.out.println(schedule.getScheduleId());
    }
}