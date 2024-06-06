package edu.hit.service;

import edu.hit.entity.Schedule;
import com.baomidou.mybatisplus.extension.service.IService;
import edu.hit.utils.Response;
import org.springframework.stereotype.Service;

/**
* @author DELL
* @description 针对表【schedule】的数据库操作Service
* @createDate 2024-06-06 11:26:41
*/

public interface ScheduleService extends IService<Schedule> {

    public Response getLimitedSchedules();
}
