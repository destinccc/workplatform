package com.uuc.common.security.utils;
  
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;  
import javax.crypto.spec.SecretKeySpec;



/** 
 * AES工具类 
 *
 */  
public class AESUtil {

    private static Logger logger = LoggerFactory.getLogger(AESUtil.class);
    // 密钥  
    public static String key = "AD42F6697B035B7580E4FEF93BE20BAD";  
    private static String charset = "utf-8";  
    // 偏移量  
    private static int offset = 16;
    private static String transformation = "AES/CBC/PKCS5Padding";
    private static String algorithm = "AES";  
  
    /** 
     * 加密 
     *  
     * @param content 
     * @return 
     */  
    public static String encrypt(String content) {  
        return encrypt(content, key);  
    }  
  
    /** 
     * 解密 
     *  
     * @param content 
     * @return 
     */  
    public static String decrypt(String content) {  
        return decrypt(content, key);  
    }  
  
    /** 
     * 加密 
     *  
     * @param content 
     *            需要加密的内容 
     * @param key 
     *            加密密码 
     * @return 
     */  
    public static String encrypt(String content, String key) {  
        try {  
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), algorithm);  
            IvParameterSpec iv = new IvParameterSpec(key.getBytes(), 0, offset);  
            Cipher cipher = Cipher.getInstance(transformation);  
            byte[] byteContent = content.getBytes(charset);  
            cipher.init(Cipher.ENCRYPT_MODE, skey, iv);// 初始化  
            byte[] result = cipher.doFinal(byteContent);  
            return new Base64().encodeToString(result); // 加密
        } catch (Exception e) {
            logger.error("AESUtils encrypt Exception",e);
        }  
        return null;  
    }  
  
    /** 
     * AES（256）解密 
     *  
     * @param content 
     *            待解密内容 
     * @param key 
     *            解密密钥 
     * @return 解密之后 
     * @throws Exception 
     */  
    public static String decrypt(String content, String key) {  
        try {  
  
            SecretKeySpec skey = new SecretKeySpec(key.getBytes(), algorithm);  
            IvParameterSpec iv = new IvParameterSpec(key.getBytes(), 0, offset);  
            Cipher cipher = Cipher.getInstance(transformation);  
            cipher.init(Cipher.DECRYPT_MODE, skey, iv);// 初始化  
            byte[] result = cipher.doFinal(new Base64().decode(content));  
            return new String(result); // 解密  
        } catch (Exception e) {
            logger.error("AESUtils encrypt Exception",e);
        }  
        return null;  
    }  
  
    public static void main(String[] args) throws Exception {  
        String s = "SR4f0zCbAJ41ue70";
        // 加密  
        System.out.println("加密前：" + s);  
        String encryptResultStr = encrypt(s,"SR4f0zCbAJ41ue70");
        System.out.println("加密后：" + encryptResultStr);  
        // 解密  
        System.out.println("解密后：" + decrypt(encryptResultStr,"SR4f0zCbAJ41ue70"));
    }  
}  