package com.uuc.alarm.domain.dto;

import com.uuc.alarm.domain.AlarmEventLog;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author HI
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class AlarmEventLogBatchInsertDto extends AlarmEventLog {
    private Long[] ids;
}
