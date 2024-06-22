package edu.hit.lvyoubackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import edu.hit.lvyoubackend.entity.Member;
import edu.hit.lvyoubackend.mapper.MemberMapper;
import edu.hit.lvyoubackend.service.MemberService;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {
}
