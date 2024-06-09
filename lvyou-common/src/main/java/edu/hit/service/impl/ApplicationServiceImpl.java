package edu.hit.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.hit.entity.Application;
import edu.hit.service.ApplicationService;
import edu.hit.mapper.ApplicationMapper;
import org.springframework.stereotype.Service;

/**
* @author DELL
* @description 针对表【application】的数据库操作Service实现
* @createDate 2024-06-06 15:20:20
*/
@Service
public class ApplicationServiceImpl extends ServiceImpl<ApplicationMapper, Application>
    implements ApplicationService{


}




