package com.uuc.alarm.vm.client.converter.am;

import com.uuc.alarm.vm.client.converter.Result;

import java.util.ArrayList;
import java.util.List;

public class DefaultAlertManagerResult extends Result<AlertManagerResultItem> {
    final List<AlertManagerResultItem> activeAlertmanagers = new ArrayList<>();

    final List<AlertManagerResultItem> droppedAlertmanagers = new ArrayList<>();

    public void addActiveManager(AlertManagerResultItem data) {
        activeAlertmanagers.add(data);
    }

    public void addDroppedManager(AlertManagerResultItem data) {
        droppedAlertmanagers.add(data);
    }

    @Override
    public List<AlertManagerResultItem> getResult() {
        return activeAlertmanagers;
    }

    @Override
    public String toString() {
        return "TargetResultItem [activeAM=" + activeAlertmanagers + ",droppedAM=" + droppedAlertmanagers + "]";
    }

}
