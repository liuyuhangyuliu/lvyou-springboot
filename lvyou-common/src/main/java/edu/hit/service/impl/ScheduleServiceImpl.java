package edu.hit.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.hit.entity.Schedule;
import edu.hit.entity.ScheduleVO;
import edu.hit.service.ScheduleService;
import edu.hit.mapper.ScheduleMapper;
import edu.hit.utils.Response;
import edu.hit.utils.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
* @author DELL
* @description 针对表【schedule】的数据库操作Service实现
* @createDate 2024-06-06 11:26:41
*/
@Slf4j
@Service
public class ScheduleServiceImpl extends ServiceImpl<ScheduleMapper, Schedule>
    implements ScheduleService{


    @Override
    public Response getLimitedSchedules() {

        QueryWrapper<Schedule> wrapper = new QueryWrapper<>();
        wrapper.last("LIMIT 10");
        List<Schedule> list = list(wrapper);
//        Schedule schedule = list.get(0);
//        JSONObject jsonObject = JSONUtil.parseObj(schedule.getImages());
//        log.info((String)jsonObject.get("image1_url"));
        List<ScheduleVO> voList = new ArrayList<>();
        for (Schedule s:list){
            voList.add(BeanUtil.copyProperties(s, ScheduleVO.class));
        }
        return new Response(StatusCode.OK,voList);
    }
}




