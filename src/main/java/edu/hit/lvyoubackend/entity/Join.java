package edu.hit.lvyoubackend.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import edu.hit.lvyoubackend.enums.JoinRole;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @TableName join
 */
@TableName(value ="join_in")
@Data
@AllArgsConstructor
public class Join implements Serializable {

    private Integer uid;

    private Integer scheduleId;

    private JoinRole role;

    private String scheduleTitle;

    @TableField(value = "join_at")
    private LocalDateTime createdAt;

    private static final long serialVersionUID = 1L;
}