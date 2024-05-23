package edu.hit.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

@TableName("user")
@Data
public class User extends Model<User> {

    @TableId
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
