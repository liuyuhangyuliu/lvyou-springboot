package edu.hit.lvyoubackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;

import edu.hit.lvyoubackend.entity.Application;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
* @author DELL
* @description 针对表【application】的数据库操作Mapper
* @createDate 2024-06-06 15:20:20
* @Entity edu.hit.entity.Application
*/
public interface ApplicationMapper extends BaseMapper<Application> {

    @Select("select a.* from schedule s join hello.application a on s.schedule_id = a.schedule_id where s.created_by = #{uid}")

    @Results({
            @Result(property = "createdBy",column = "created_by",typeHandler = JacksonTypeHandler.class)
    })
    public List<Application>  getApplicationIShouldAudit(@Param("uid") Integer uid);
}




