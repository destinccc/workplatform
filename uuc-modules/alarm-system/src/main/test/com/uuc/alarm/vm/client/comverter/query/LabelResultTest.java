package com.uuc.alarm.vm.client.comverter.query;

import com.uuc.alarm.vm.client.converter.ConvertUtil;
import com.uuc.alarm.vm.client.converter.label.DefaultLabelResult;
import org.junit.jupiter.api.Test;

public class LabelResultTest {
    @SuppressWarnings({"FieldCanBeLocal", "SpellCheckingInspection"})
    private final String testLabelData = "{\"status\":\"success\",\"data\":[\"person-application-1.5-5dcc65c754-7ztnz\",\"person-application-1.5-5dcc65c754-8gb82\",\"person-application-1.5-5dcc65c754-8xh22\"]}";

    @Test
    public void testParser() {
        DefaultLabelResult result = ConvertUtil.convertLabelResultString(testLabelData);
        System.out.println("-----" + result.getResult().size());
        for (String data : result.getResult()) {
            System.out.println("=======>" + data);
        }
    }
}
