package edu.hit.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduleVO {

    private UserBO userBO;

    private Integer scheduleId;

    private String destination;

    private LocalDateTime departAt;

    private String title;

    private String description;

    private String departLocation;

    private String images;


}
