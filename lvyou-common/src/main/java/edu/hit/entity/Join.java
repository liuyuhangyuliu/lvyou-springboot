package edu.hit.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @TableName join
 */
@TableName(value ="join")
@Data
@AllArgsConstructor
public class Join implements Serializable {

    private Integer uid;

    private Integer scheduleId;

    private String role;

    private LocalDateTime joinAt;

    private static final long serialVersionUID = 1L;
}