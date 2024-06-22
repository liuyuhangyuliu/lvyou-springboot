package edu.hit.lvyoubackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.hit.lvyoubackend.entity.Schedule;
import edu.hit.lvyoubackend.entity.ScheduleVO;
import edu.hit.lvyoubackend.utils.Response;


import java.util.List;

/**
* @author DELL
* @description 针对表【schedule】的数据库操作Service
* @createDate 2024-06-06 11:26:41
*/

public interface ScheduleService extends IService<Schedule> {

    public Response getLimitedSchedules();

    public Object getScheduleDetails(Integer uid,Integer scheduleId);

    ScheduleVO getScheduleVObyId(Integer scheduleId);

    List<ScheduleVO> getScheduleByPage(Integer page,Integer size);
}
