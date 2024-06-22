package edu.hit.lvyoubackend.entity;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;

import edu.hit.lvyoubackend.enums.ApplicationState;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @TableName application
 */
@TableName(value ="application",autoResultMap = true)
@Data
@NoArgsConstructor
public class Application implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer applyId;

    private Integer uid;

    private Integer scheduleId;

    private String scheduleTitle;

    private LocalDateTime createdAt;

    private ApplicationState state;


    @TableField(typeHandler = JacksonTypeHandler.class)
    private JSONObject createdBy;

    private static final long serialVersionUID = 1L;

    public Application(Integer uid, Integer scheduleId, LocalDateTime createdAt,JSONObject createdBy,String scheduleTitle) {
        this.uid = uid;
        this.scheduleId = scheduleId;
        this.createdAt = createdAt;
        this.createdBy = createdBy;
        this.scheduleTitle = scheduleTitle;
    }
}