package com.uuc.alarm.vm.client.comverter.query;

import com.uuc.alarm.vm.client.converter.ConvertUtil;
import com.uuc.alarm.vm.client.converter.am.AlertManagerResultItem;
import com.uuc.alarm.vm.client.converter.am.DefaultAlertManagerResult;
import org.junit.jupiter.api.Test;

public class AlertManagerResultTest {
    @SuppressWarnings("FieldCanBeLocal")
    private final String testAlertManagerData = "{\"status\":\"success\",\"data\":{\"activeAlertmanagers\":[]}}";

    @Test
    public void testParser() {
        DefaultAlertManagerResult result = ConvertUtil.convertAlertManagerResultString(testAlertManagerData);
        System.out.println("-----" + result.getResult().size());

        for (AlertManagerResultItem data : result.getResult()) {
            System.out.println("=======>\n" + data);
        }
    }
}
