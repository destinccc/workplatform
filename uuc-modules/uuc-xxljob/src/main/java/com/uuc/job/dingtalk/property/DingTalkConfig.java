
package com.uuc.job.dingtalk.property;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.pinyin.PinyinUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Data
@Component
@ConfigurationProperties(prefix = "dingtalk")
public class DingTalkConfig {

    // 内部应用key
    private String appKey;

    // 内部应用secret
    private String appSecret;

    public static void main(String[] args) {
        System.out.println(StrUtil.trim(PinyinUtil.getPinyin("朱加敏").replaceAll(" ","")));
    }
}
