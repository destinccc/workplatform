package com.uuc.alarm.service.impl;

import cn.hutool.http.HttpUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.uuc.alarm.domain.vo.MetricDetailVo;
import com.uuc.alarm.domain.vo.MetricDetailVoResult;
import com.uuc.alarm.service.IMetricService;
import com.uuc.alarm.vm.client.builder.QueryBuilderType;
import com.uuc.alarm.vm.client.builder.RangeQueryBuilder;
import com.uuc.alarm.vm.client.converter.ConvertUtil;
import com.uuc.alarm.vm.client.converter.query.DefaultQueryResult;
import com.uuc.alarm.vm.client.converter.query.MatrixData;
import com.uuc.alarm.vm.client.converter.query.QueryResultItemValue;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RefreshScope
public class MetricServiceImpl implements IMetricService {
    private static final Long INTERVAL = 7200L;

    private static final Integer OUTPUT_NUM = 120;

    @Value(value = "${prometheus.url:http://10.128.120.26:8082/select/0/prometheus}")
    private String PROM_BASE_URL;

    private static final String TODAY = "today";
    private static final String ONE_DAY_AGO = "oneDayAgo";
    private static final String SEVEN_DAYS_AGO = "sevenDaysAgo";


    @Override
    public List<MetricDetailVoResult> fetchMetricDetail(String metricName, String hostAddress) {
        String queryParam = metricName + "{resource_ident=\"" + hostAddress + "\"}";
        return getMetricDetailResult(queryParam);

    }

    private List<MetricDetailVoResult> getMetricDetailResult(String queryParam) {
        Map<String, List<MetricDetailVo>> map = Maps.newHashMap();
        Map<String, Long> unitTimeMap = getUnitTimeMap();
        List<MetricDetailVoResult> results = Lists.newArrayList();

        unitTimeMap.forEach((type, unit) -> {
            Map<Long, MetricDetailVo> metricDetailVoMap = handleMetricResult(queryParam,unit);
            List<MetricDetailVo> metricDetailVoList = fillMetricResult(metricDetailVoMap, unit);
            map.put(type, metricDetailVoList);
        });

        int size = map.get(TODAY).size();
        for (int i = 0; i < size; i++) {
            MetricDetailVoResult metricDetailVoResult = new MetricDetailVoResult();
            metricDetailVoResult.setTimestamp(map.get(TODAY).get(i).getTimestamp());
            metricDetailVoResult.setValue(map.get(TODAY).get(i).getValue());
            metricDetailVoResult.setValue1(map.get(ONE_DAY_AGO).get(i).getValue());
            metricDetailVoResult.setValue7(map.get(SEVEN_DAYS_AGO).get(i).getValue());
            results.add(metricDetailVoResult);
        }
        return results;
    }

    private List<MetricDetailVo> fillMetricResult(Map<Long, MetricDetailVo> metricDetailVoMap, Long timestamp) {
        List<Long> timeStampList = Lists.newArrayList();
        for (int i = 0; i < OUTPUT_NUM; i++) {
            timeStampList.add(timestamp);
            timestamp = timestamp + 60;
        }
        timeStampList.forEach(time -> {
            if (!metricDetailVoMap.containsKey(time)) {
                MetricDetailVo metricDetailVo = metricDetailVoMap.get(time - 60);
                if(Objects.isNull(metricDetailVo)){
                    metricDetailVo =new MetricDetailVo();
                    metricDetailVo.setValue(0);
                }
                metricDetailVo.setTimestamp(time);
                metricDetailVoMap.put(time, metricDetailVo);
            }
        });
        return metricDetailVoMap.values().stream().sorted(Comparator.comparing(MetricDetailVo::getTimestamp)).limit(120).collect(Collectors.toList());
    }

    Map<String, Long> getUnitTimeMap() {
        Map<String, Long> resultMap = Maps.newHashMap();
        long currentUnit = getMinuteTimestamp();
        resultMap.put(TODAY, currentUnit);
        resultMap.put(ONE_DAY_AGO, currentUnit - 60 * 60 * 24);
        resultMap.put(SEVEN_DAYS_AGO, currentUnit - 60 * 60 * 24 * 7);
        return resultMap;
    }


    long getMinuteTimestamp() {
        long currentTime = System.currentTimeMillis() / 1000;
        long minute = currentTime / 60;
        return minute * 60;
    }

    /**
     * 将Vm指标数据转换为业务数据
     */
    private Map<Long, MetricDetailVo> handleMetricResult(String queryParam, Long unit) {
        Map<Long, MetricDetailVo> metricDetailVoMap = Maps.newHashMap();
        List<MatrixData> scalarDataList;
        try {
            scalarDataList = getQueryResult(queryParam, unit);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        if (CollectionUtils.isEmpty(scalarDataList)) {
            return Collections.emptyMap();
        }
        for (MatrixData matrixData : scalarDataList) {
            List<QueryResultItemValue> queryResultItemValueList = Arrays.asList(matrixData.getDataValues());
            queryResultItemValueList.forEach(queryResultItemValue -> {
                MetricDetailVo metricDetailVo = new MetricDetailVo();
                metricDetailVo.setValue(queryResultItemValue.getValue());
                long timestamp = queryResultItemValue.getTimestamp();
                metricDetailVo.setTimestamp(timestamp);
                metricDetailVoMap.put(timestamp, metricDetailVo);
            });
        }
        return metricDetailVoMap;
    }

    /**
     * 查询指标数据结果
     *
     * @throws MalformedURLException MalformedURLException
     */

    private List<MatrixData> getQueryResult(String queryParam, Long unit) throws MalformedURLException {

        URI targetUri = getTargetUrI(unit, queryParam);
        log.info("targetUrl:{}", targetUri);
        String result = HttpUtil.get(targetUri.toURL().toString());
        log.info("result:{}", result);
        DefaultQueryResult<MatrixData> metricResult = ConvertUtil.convertQueryResultString(result);
        log.info("metricResult:{}", metricResult.getResult());
        return metricResult.getResult();
    }

    /**
     * 获取VM URI
     *
     * @param unit       查询时间单元
     * @param queryParam 查询参数
     * @return URI
     */
    URI getTargetUrI(Long unit, String queryParam) {
        RangeQueryBuilder rangeQueryBuilder = QueryBuilderType.RangeQuery.newInstance(PROM_BASE_URL);
        Pair<Long, Long> timePair = getStartTimeAndEndTime(unit);
        rangeQueryBuilder.withQuery(queryParam).withStartEpochTime(timePair.getLeft())
                .withEndEpochTime(timePair.getRight())
                .withStepTime("60s");
        return rangeQueryBuilder.build();
    }

    /**
     * 获取查询开始和结束时间
     */
    Pair<Long, Long> getStartTimeAndEndTime(long unit) {
        Long startTime = unit - INTERVAL;
        Long endTime = unit;
        return Pair.of(startTime, endTime);
    }
}
