package edu.hit.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @TableName application
 */
@TableName(value ="application")
@Data
@NoArgsConstructor
public class Application implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer applyId;

    private Integer uid;

    private Integer scheduleId;

    private LocalDateTime createdAt;

    private Boolean approval;

    private static final long serialVersionUID = 1L;

    public Application(Integer uid, Integer scheduleId, LocalDateTime createdAt) {
        this.uid = uid;
        this.scheduleId = scheduleId;
        this.createdAt = createdAt;
    }
}