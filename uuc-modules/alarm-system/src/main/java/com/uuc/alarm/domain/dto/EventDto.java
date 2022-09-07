package com.uuc.alarm.domain.dto;

import com.uuc.alarm.domain.Event;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author HI
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EventDto extends Event {
    private Long[] ids;
}
