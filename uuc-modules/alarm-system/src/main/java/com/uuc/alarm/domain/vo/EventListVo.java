package com.uuc.alarm.domain.vo;

import com.uuc.alarm.util.DefaultExcelHandlerAdapter;
import com.uuc.alarm.util.ListExcelHandlerAdapter;
import com.uuc.alarm.util.TimestampExcelHandlerAdapter;
import com.uuc.common.core.annotation.Excel;
import lombok.Data;

import java.util.List;

@Data
public class EventListVo {
    private Long id;
    private Long resourceId;
    private Long strategyId;
    private String endpoint;
    @Excel(name = "告警时间", handler = TimestampExcelHandlerAdapter.class)
    private Long etime;
    @Excel(name = "告警等级", readConverterExp = "0=P0,1=P1,2=P2,3=P3")
    private Integer priority;
    @Excel(name = "资源名称", width = 57)
    private String resourceName;
    @Excel(name = "触发策略")
    private String strategy;
    @Excel(name = "告警摘要", args = {"String"}, width = 32)
    private String summary;
    @Excel(name = "告警次数")
    private Integer alarmTimes;
    @Excel(name = "告警状态", readConverterExp = "alert=活跃告警,recovery=告警恢复,stop=已关闭告警,noStrategy=策略删除,noHost=资源删除")
    private String eventType;
    @Excel(name = "通知状态", readConverterExp = "0=处理中,1=已发送,2=已回调,4=已屏蔽,8=已收敛,16=无接收人,32=已升级")
    private Integer notifyStatus;
    @Excel(name = "通知组", handler = ListExcelHandlerAdapter.class)
    private List<String> notifyGroup;
    @Excel(name = "通知人", handler = ListExcelHandlerAdapter.class)
    private List<String> notifyUser;
    @Excel(name = "通知时间", handler = TimestampExcelHandlerAdapter.class)
    private Long notifyTime;
    @Excel(name = "关闭时间", handler = TimestampExcelHandlerAdapter.class)
    private Long closeTime;
    @Excel(name = "关闭人", handler = DefaultExcelHandlerAdapter.class, args = {"String"})
    private String closer;
}
