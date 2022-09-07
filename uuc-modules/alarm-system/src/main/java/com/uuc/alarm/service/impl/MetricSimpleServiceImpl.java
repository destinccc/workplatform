package com.uuc.alarm.service.impl;

import com.uuc.alarm.mapper.MetricSimpleMapper;
import com.uuc.alarm.service.IMetricSimpleService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class MetricSimpleServiceImpl implements IMetricSimpleService {
    private final MetricSimpleMapper metricSimpleMapper;

    public MetricSimpleServiceImpl(MetricSimpleMapper metricSimpleMapper) {
        this.metricSimpleMapper = metricSimpleMapper;
    }

    @Override
    public String fetchMetricTranslation(String metricName) {
        String result = metricSimpleMapper.fetchByMetricName(metricName);
        if (Objects.isNull(result)) {
            return "";
        }
        return result;
    }
}
