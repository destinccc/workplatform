package com.uuc.alarm.service;

import com.uuc.alarm.domain.vo.MetricDetailVoResult;

import java.util.List;

public interface IMetricService {

    List<MetricDetailVoResult> fetchMetricDetail(String metricName, String hostAddress);

}
