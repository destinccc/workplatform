package com.uuc.alarm.vm.client.builder;

import cn.hutool.http.HttpUtil;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URI;

public class UriBuilderTest {
    private static final String PROM_BASE_URL = "http://10.128.120.26:8082/select/0/prometheus";



    @Test
    public void testRangeQueryBuilder() throws MalformedURLException {
        RangeQueryBuilder rangeQueryBuilder = QueryBuilderType.RangeQuery.newInstance(PROM_BASE_URL);
        URI targetUri = rangeQueryBuilder.withQuery("irate(received_api_call_total[60s])")
                .withStartEpochTime(System.currentTimeMillis()  - 60 * 10*1000)
                .withEndEpochTime(System.currentTimeMillis() )
                .withStepTime("60s")
                .build();
        System.out.println(targetUri.toURL());
    }

    @Test
    public void testRangeQueryBuilderByHost() throws  MalformedURLException{
        RangeQueryBuilder rangeQueryBuilder = QueryBuilderType.RangeQuery.newInstance(PROM_BASE_URL);
        URI targetUri = rangeQueryBuilder.withQuery("cpu_usage{resource_ident=\"10.128.20.119\"}")
                .withStartEpochTime(System.currentTimeMillis()/1000 -60 * 10)
                .withEndEpochTime(System.currentTimeMillis()/1000 )
                .withStepTime("1d")
                .build();
        System.out.println(targetUri.toURL());
    }

    @Test
    public void testInstantQueryBuilder() throws MalformedURLException {
        InstantQueryBuilder iqb = QueryBuilderType.InstantQuery.newInstance(PROM_BASE_URL);
        URI targetUri = iqb.withQuery("irate(received_api_call_total[60s])").build();
        String result = HttpUtil.get(targetUri.toURL().toString());
        System.out.println(result);
//        System.out.println(targetUri.toURL().toString());
    }

    @Test
    public void testSeriesMetaQueryBuilder() throws MalformedURLException {
        SeriesMetaQueryBuilder smqb = QueryBuilderType.SeriesMetadaQuery.newInstance(PROM_BASE_URL);
        URI targetUri = smqb.withSelector("match[]=up&match[]=process_start_time_seconds{job=\"prometheus\"}").build();
        System.out.println(targetUri.toURL());
    }

    @Test
    public void testLabelMetaQueryBuilder() throws MalformedURLException {
        LabelMetaQueryBuilder lmqb = QueryBuilderType.LabelMetadaQuery.newInstance(PROM_BASE_URL);
        URI targetUri = lmqb.withLabel("pod").build();
        System.out.println(targetUri.toURL());
    }

    @Test
    public void testStatusMetaQueryBuilder() throws MalformedURLException {
        StatusMetaQueryBuilder smqb = QueryBuilderType.StatusMetadaQuery.newInstance(PROM_BASE_URL);
        URI targetUri = smqb.build();
        System.out.println(targetUri.toURL());
    }

    @Test
    public void testTargetsMetaQueryBuilder() throws MalformedURLException {
        TargetMetaQueryBuilder tmqb = QueryBuilderType.TargetMetadaQuery.newInstance(PROM_BASE_URL);
        URI targetUri = tmqb.build();
        System.out.println(targetUri.toURL());
    }

    @Test
    public void testAlertManagerMetaQueryBuilder() throws MalformedURLException {
        AlertManagerMetaQueryBuilder ammqb = QueryBuilderType.AlertManagerMetadaQuery.newInstance(PROM_BASE_URL);
        URI targetUri = ammqb.build();
        System.out.println(targetUri.toURL());
    }
}
