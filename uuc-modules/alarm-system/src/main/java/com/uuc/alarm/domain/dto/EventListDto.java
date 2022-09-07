package com.uuc.alarm.domain.dto;

import com.uuc.alarm.domain.Event;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = false)
public class EventListDto extends Event {
    /**
     * 类型，cur or all
     **/
    private String type;
    private String query;
    private Long hours;
    private Long startTime;
    private Long endTime;
    private List<Integer> priorityList;
    private Set<String> endpointList;
    private String priorities;
    private Integer pageNum;
    private Integer pageSize;
    /**
     * 导出标志，默认false
     */
    private Boolean exportFlag;
}
