package com.uuc.alarm.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MetricSimpleMapper {
    @Select("select metric_translation from metric_simple s where s.metric_name =#{metricName}")
    String fetchByMetricName(String metricName);
}
