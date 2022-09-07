package com.uuc.alarm.vm.client.comverter.query;

import com.uuc.alarm.vm.client.converter.ConvertUtil;
import com.uuc.alarm.vm.client.converter.Data;
import com.uuc.alarm.vm.client.converter.query.DefaultQueryResult;
import com.uuc.alarm.vm.client.converter.query.ScalarData;
import org.junit.jupiter.api.Test;

public class MatrixResultTest {

    private final String testScalarData = "{\"status\":\"success\",\"data\":{\"resultType\":\"matrix\",\"result\":[{\"metric\":{\"__name__\":\"cpu_usage\",\"__endpoint__\":\"10.128.20.119\",\"resource_ident\":\"10.128.20.119\"},\"values\":[[1659594435,\"0.450000000225\"],[1659594735,\"0.46250000014\"]]},{\"metric\":{\"__name__\":\"up\",\"job\":\"node\",\"instance\":\"localhost:9091\"},\"values\":[[1435781430.781,\"0\"],[1435781445.781,\"0\"],[1435781460.781,\"1\"]]}]}}";

    @SuppressWarnings("FieldCanBeLocal")
    private final String testMatrixData = "{\"status\":\"success\",\"data\":{\"resultType\":\"matrix\",\"result\":[{\"metric\":{\"__name__\":\"cpu_usage\",\"__endpoint__\":\"10.128.20.119\",\"resource_ident\":\"10.128.20.119\"},\"values\":[[1659601573,\"0.416666666667\"],[1659601633,\"0.370833333388\"]]}]}}";

    @Test
    public void testParser() {
        DefaultQueryResult<ScalarData> result = ConvertUtil.convertQueryResultString(testMatrixData);
        System.out.println("-----" + result.getResult().size());
        for (Data data : result.getResult()) {
            System.out.println("=======>" + data);
        }
    }
}
