package edu.hit.mapper;

import edu.hit.entity.Schedule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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




