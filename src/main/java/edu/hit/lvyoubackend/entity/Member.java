package edu.hit.lvyoubackend.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import edu.hit.lvyoubackend.enums.JoinRole;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("member")
public class Member {
    private String username;
    @TableField(value ="avatarSrc")
    private String avatarSrc;
    private LocalDateTime joinAt;
    private JoinRole role;
}
