package com.uuc.alarm.vm.client.converter.series;

import com.uuc.alarm.vm.client.converter.Result;

import java.util.ArrayList;
import java.util.List;

public class DefaultSeriesResult extends Result<SeriesResultItem> {
    final List<SeriesResultItem> result = new ArrayList<>();

    public void addData(SeriesResultItem data) {
        result.add(data);
    }

    @Override
    public List<SeriesResultItem> getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "SeriesResultItem [result=" + result + "]";
    }

}
