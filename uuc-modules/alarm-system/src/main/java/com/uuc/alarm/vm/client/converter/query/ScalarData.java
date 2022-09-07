package com.uuc.alarm.vm.client.converter.query;

import com.uuc.alarm.vm.client.converter.Data;

public class ScalarData extends QueryResultItemValue implements Data {

    public ScalarData(long timestamp, double value) {
        super(timestamp, value);
    }

}
