package com.uuc.alarm.vm.client.comverter.query;

import com.uuc.alarm.vm.client.converter.ConvertUtil;
import com.uuc.alarm.vm.client.converter.Data;
import com.uuc.alarm.vm.client.converter.series.DefaultSeriesResult;
import org.junit.jupiter.api.Test;

public class SeriesResultTest {
    @SuppressWarnings({"FieldCanBeLocal", "SpellCheckingInspection"})
    private final String testSeriesData = "{\"status\":\"success\",\"data\":[{\"__name__\":\"up\",\"endpoint\":\"http\",\"instance\":\"10.244.1.4:19000\",\"job\":\"person-application-1-5\",\"namespace\":\"default\",\"pod\":\"person-application-1.5-5dcc65c754-8xh22\",\"service\":\"person-application-1-5\"},{\"__name__\":\"up\",\"endpoint\":\"http\",\"instance\":\"10.244.2.4:19000\",\"job\":\"person-application-1-5\",\"namespace\":\"default\",\"pod\":\"person-application-1.5-5dcc65c754-8gb82\",\"service\":\"person-application-1-5\"},{\"__name__\":\"up\",\"endpoint\":\"http\",\"instance\":\"10.244.4.4:19000\",\"job\":\"person-application-1-5\",\"namespace\":\"default\",\"pod\":\"person-application-1.5-5dcc65c754-7ztnz\",\"service\":\"person-application-1-5\"}]}";

    @Test
    public void testParser() {
        DefaultSeriesResult result = ConvertUtil.convertSeriesResultString(testSeriesData);
        System.out.println("-----" + result.getResult().size());
        for (Data data : result.getResult()) {
            System.out.println("=======>" + data);
        }
    }
}
