package edu.hit.entity;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import edu.hit.enums.ScheduleState;
import lombok.Data;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;

/**
 * @TableName schedule
 * 字段名必须用驼峰
 * 吗？
 * 驼峰字段名自动对应表的列名
 * 试了将实体类的字段改成下划线式的，但是就会是null，即便加了注解指定列的名称
 */
@TableName(value ="schedule")
@Data
public class Schedule implements Serializable {

    @TableId(value = "schedule_id",type = IdType.AUTO)
    private Integer scheduleId;

    private String destination;

    @TableField("created_at")
    private LocalDateTime createdAt;

    @TableField("depart_at")
    private LocalDateTime departAt;

    private String title;

    private String description;

    private String departLocation;

    private String details;

    private String images;

    private Integer maxNumber;

    private Integer currentNumber;

    private ScheduleState state;

    private Integer createdBy;

    private static final long serialVersionUID = 1L;
}