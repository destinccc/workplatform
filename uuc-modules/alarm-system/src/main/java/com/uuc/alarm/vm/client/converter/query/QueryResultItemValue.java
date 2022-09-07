package com.uuc.alarm.vm.client.converter.query;

public class QueryResultItemValue {
    private long timestamp;
    private double value;

    public QueryResultItemValue(long timestamp, double value) {
        super();
        this.timestamp = timestamp;
        this.value = value;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "QueryResultItemValue [timestamp=" + timestamp + ", value=" + value + "]";
    }

}
