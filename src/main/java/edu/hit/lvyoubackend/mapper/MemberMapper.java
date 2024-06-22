package edu.hit.lvyoubackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import edu.hit.lvyoubackend.entity.Member;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemberMapper extends BaseMapper<Member> {

}
