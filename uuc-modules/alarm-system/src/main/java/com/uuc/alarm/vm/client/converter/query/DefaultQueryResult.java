package com.uuc.alarm.vm.client.converter.query;

import com.uuc.alarm.vm.client.converter.Data;
import com.uuc.alarm.vm.client.converter.Result;

import java.util.ArrayList;
import java.util.List;

public class DefaultQueryResult<T extends Data> extends Result<T> {

    final List<T> result = new ArrayList<>();

    public void addData(T data) {
        result.add(data);
    }

    @Override
    public List<T> getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "DefaultQueryResult [result=" + result + "]";
    }


}
