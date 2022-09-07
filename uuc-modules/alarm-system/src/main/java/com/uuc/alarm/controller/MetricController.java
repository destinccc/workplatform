package com.uuc.alarm.controller;

import com.google.common.collect.Maps;
import com.uuc.alarm.service.IMetricService;
import com.uuc.alarm.service.IMetricSimpleService;
import com.uuc.common.core.web.controller.BaseController;
import com.uuc.common.core.web.domain.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/metric")
public class MetricController extends BaseController {

    private IMetricService metricService;

    final
    IMetricSimpleService metricSimpleService;

    public MetricController(IMetricService metricService, IMetricSimpleService metricSimpleService) {
        this.metricSimpleService = metricSimpleService;
        this.metricService = metricService;
    }

    @GetMapping("/detail")
    public AjaxResult showDetail(String metricName, String hostAddress) {
        Map<String, Object> result = Maps.newHashMap();
        String metricTranslation = metricSimpleService.fetchMetricTranslation(metricName);
        result.put("metricTranslation", metricTranslation);
        result.put("list", metricService.fetchMetricDetail(metricName, hostAddress));
        return AjaxResult.success(result);

    }
}
