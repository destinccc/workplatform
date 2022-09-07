package com.uuc.alarm.vm.client.converter.targets;

import com.uuc.alarm.vm.client.converter.Result;

import java.util.ArrayList;
import java.util.List;

public class DefaultTargetResult extends Result<TargetResultItem> {
    final List<TargetResultItem> activeTargets = new ArrayList<>();
    final List<TargetResultItem> droppedTargets = new ArrayList<>();

    public void addActiveTarget(TargetResultItem data) {
        activeTargets.add(data);
    }

    public void addDroppedTarget(TargetResultItem data) {
        droppedTargets.add(data);
    }

    @Override
    public List<TargetResultItem> getResult() {
        return activeTargets;
    }

    @Override
    public String toString() {
        return "TargetResultItem [activeTargets=" + activeTargets + ",droppedTargets=" + droppedTargets + "]";
    }

}
