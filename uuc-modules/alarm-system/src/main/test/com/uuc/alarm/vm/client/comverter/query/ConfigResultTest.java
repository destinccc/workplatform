package com.uuc.alarm.vm.client.comverter.query;

import com.uuc.alarm.vm.client.converter.ConvertUtil;
import com.uuc.alarm.vm.client.converter.status.DefaultConfigResult;
import org.junit.jupiter.api.Test;

public class ConfigResultTest {
    @SuppressWarnings("FieldCanBeLocal")
    private final String testConfigData = "{\"status\":\"success\",\"data\":{\"yaml\":\"global:\\n  scrape_interval: 30s\\n  scrape_timeout: 10s\\n  evaluation_interval: 30s\\nscrape_configs:\\n- job_name: default/person-application-1.5-monitor/0\\n  scrape_interval: 10s\\n  scrape_timeout: 10s\\n  metrics_path: /prometheus\\n  scheme: http\\n  kubernetes_sd_configs:\\n  - api_server: null\\n    role: endpoints\\n    namespaces:\\n      names:\\n      - default\\n  relabel_configs:\\n  - source_labels: [__meta_kubernetes_service_label_run]\\n    separator: ;\\n    regex: person-application-1.5\\n    replacement: $1\\n    action: keep\\n  - source_labels: [__meta_kubernetes_endpoint_port_name]\\n    separator: ;\\n    regex: http\\n    replacement: $1\\n    action: keep\\n  - source_labels: [__meta_kubernetes_namespace]\\n    separator: ;\\n    regex: (.*)\\n    target_label: namespace\\n    replacement: $1\\n    action: replace\\n  - source_labels: [__meta_kubernetes_pod_name]\\n    separator: ;\\n    regex: (.*)\\n    target_label: pod\\n    replacement: $1\\n    action: replace\\n  - source_labels: [__meta_kubernetes_service_name]\\n    separator: ;\\n    regex: (.*)\\n    target_label: service\\n    replacement: $1\\n    action: replace\\n  - source_labels: [__meta_kubernetes_service_name]\\n    separator: ;\\n    regex: (.*)\\n    target_label: job\\n    replacement: ${1}\\n    action: replace\\n  - separator: ;\\n    regex: (.*)\\n    target_label: endpoint\\n    replacement: http\\n    action: replace\\n\"}}";

    @Test
    public void testParser() {
        DefaultConfigResult result = ConvertUtil.convertConfigResultString(testConfigData);
        System.out.println("-----" + result.getResult().size());
        for (String data : result.getResult()) {
            System.out.println("=======>\n" + data);
        }
    }
}
