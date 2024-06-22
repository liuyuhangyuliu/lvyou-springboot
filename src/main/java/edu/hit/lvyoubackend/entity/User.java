package edu.hit.lvyoubackend.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;
import lombok.experimental.Accessors;

@TableName("user")
@Data
//setter返回此对象，可以链式调用setter
@Accessors(chain = true)
public class User extends Model<User> {

    @TableId(type = IdType.AUTO)
    private Long uid;
    private String username;
    private String email;
    private String password;
    @TableField("coverSrc")
    private String coverSrc;
    @TableField("avatarSrc")
    private String avatarSrc;
    private String salt;


}
