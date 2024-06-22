package edu.hit.lvyoubackend.entity;


import edu.hit.lvyoubackend.enums.ScheduleState;
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

    private Integer maxNumber;

    private Integer currentNumber;
    private ScheduleState state;

    private String images;


}
