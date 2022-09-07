//package com.uuc.auth.controller;
//
//import com.google.code.kaptcha.Producer;
//import com.uuc.common.core.constant.Constants;
//import com.uuc.common.core.domain.R;
//import com.uuc.common.core.utils.sign.Base64;
//import com.uuc.common.core.utils.uuid.IdUtils;
//import com.uuc.common.redis.service.RedisService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.util.FastByteArrayOutputStream;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.annotation.Resource;
//import javax.imageio.ImageIO;
//import javax.servlet.http.HttpServletResponse;
//import java.awt.image.BufferedImage;
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.TimeUnit;
//
///**
// * 验证码操作处理
// *
// * @author xjy
// */
//@RestController
//public class CaptchaController
//{
//    @Resource(name = "captchaProducer")
//    private Producer captchaProducer;
//
//    @Resource(name = "captchaProducerMath")
//    private Producer captchaProducerMath;
//    @Autowired
//    private RedisService redisService;
//
//    // 验证码类型
//    private String captchaType="char";
//
//    /**
//     * 生成验证码
//     */
//    @PostMapping("/captchaImage")
//    public R<?> getCode(HttpServletResponse response) throws IOException
//    {
//        // 保存验证码信息
//        String uuid = IdUtils.simpleUUID();
//        String verifyKey = Constants.CAPTCHA_CODE_KEY + uuid;
//
//        String capStr = null, code = null;
//        BufferedImage image = null;
//
//        // 生成验证码
//        if ("math".equals(captchaType))
//        {
//            String capText = captchaProducerMath.createText();
//            capStr = capText.substring(0, capText.lastIndexOf("@"));
//            code = capText.substring(capText.lastIndexOf("@") + 1);
//            image = captchaProducerMath.createImage(capStr);
//        }
//        else if ("char".equals(captchaType))
//        {
//            capStr = code = captchaProducer.createText();
//            image = captchaProducer.createImage(capStr);
//        }
//
//        redisService.setCacheObject(verifyKey, code, Constants.CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
//        // 转换流信息写出
//        FastByteArrayOutputStream os = new FastByteArrayOutputStream();
//        try
//        {
//            ImageIO.write(image, "jpg", os);
//        }
//        catch (IOException e)
//        {
//            return R.fail(e.getMessage());
//        }
//
//        Map codeMap=new HashMap<>();
//        codeMap.put("uuid", uuid);
//        codeMap.put("img", Base64.encode(os.toByteArray()));
//        return R.ok(codeMap);
//    }
//}
