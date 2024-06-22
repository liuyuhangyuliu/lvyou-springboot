package edu.hit.lvyoubackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import edu.hit.lvyoubackend.entity.Application;
import edu.hit.lvyoubackend.mapper.ApplicationMapper;
import edu.hit.lvyoubackend.service.ApplicationService;
import edu.hit.lvyoubackend.utils.Response;
import edu.hit.lvyoubackend.utils.StatusCode;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author DELL
* @description 针对表【application】的数据库操作Service实现
* @createDate 2024-06-06 15:20:20
*/
@Service
public class ApplicationServiceImpl extends ServiceImpl<ApplicationMapper, Application>
    implements ApplicationService {


    @Override
    public Response getApplicationIShouldAudit(Integer uid) {
        ApplicationMapper applicationMapper = this.baseMapper;
        List<Application> list = applicationMapper.getApplicationIShouldAudit(uid);
        return new Response(StatusCode.OK.set("000","获取我要审核的申请"),list);

    }
}




