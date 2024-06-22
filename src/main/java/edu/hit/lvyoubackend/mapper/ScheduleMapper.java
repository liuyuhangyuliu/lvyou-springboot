package edu.hit.lvyoubackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import edu.hit.lvyoubackend.entity.Schedule;
import org.apache.ibatis.annotations.Mapper;


/**
* @author DELL
* @description 针对表【schedule】的数据库操作Mapper
* @createDate 2024-06-06 11:26:41
* @Entity edu.hit.entity.Schedule
*/
@Mapper
public interface ScheduleMapper extends BaseMapper<Schedule> {

}




