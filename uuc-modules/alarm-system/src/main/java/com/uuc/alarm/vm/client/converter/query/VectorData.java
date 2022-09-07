package com.uuc.alarm.vm.client.converter.query;

import com.uuc.alarm.vm.client.converter.Data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class VectorData implements Data {

    private Map<String, String> metric = new HashMap<>();

    private QueryResultItemValue dataValue;


    public Map<String, String> getMetric() {
        return metric;
    }

    public void setMetric(Map<String, String> metric) {
        this.metric = metric;
    }

    public QueryResultItemValue getDataValue() {
        return dataValue;
    }

    public void setDataValue(QueryResultItemValue value) {
        this.dataValue = value;
    }


    public double getValue() {
        return dataValue.getValue();
    }

    public long getTimestamps() {
        return dataValue.getTimestamp();
    }

    public String getFormattedTimestamps(String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(new Date(Math.round(dataValue.getTimestamp() * 1000L)));
    }

    @Override
    public String toString() {
        return "VectorData [metric=" + metric + ", dataValue=" + dataValue + "]";
    }


}
