package edu.hit.lvyoubackend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.hit.lvyoubackend.entity.Application;
import edu.hit.lvyoubackend.utils.Response;


/**
* @author DELL
* @description 针对表【application】的数据库操作Service
* @createDate 2024-06-06 15:20:20
*/
public interface ApplicationService extends IService<Application> {

    public Response getApplicationIShouldAudit(Integer uid);
}
