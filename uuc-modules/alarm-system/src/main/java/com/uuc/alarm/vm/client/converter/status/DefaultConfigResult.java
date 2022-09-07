package com.uuc.alarm.vm.client.converter.status;

import com.uuc.alarm.vm.client.converter.Result;

import java.util.ArrayList;
import java.util.List;

public class DefaultConfigResult extends Result<String> {
    final List<String> result = new ArrayList<>();

    public void addData(String data) {
        result.add(data);
    }

    @Override
    public List<String> getResult() {
        return result;
    }

    @Override
    public String toString() {
        return "DefaultConfigResult [result=" + result + "]";
    }

}
