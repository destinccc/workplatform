package com.uuc.auth.utils;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;
import javax.crypto.Cipher;
import org.apache.commons.codec.binary.Base64;
/**
 * @author yao
 * @create 2018/1/20
 */
public class RSAEncrypt {
    private static final String ALGORITHM = "RSA";
    private static final int MAX_ENCRYPT_BLOCK = 117;
    private static final int MAX_DECRYPT_BLOCK = 128;
    private static Map<Integer, String> keyMap = new HashMap<Integer, String>();  //用于封装随机产生的公钥与私钥
 
    public static final Integer PUBLICKEY = 0;//0表示公钥
    public static final Integer PRIVATEKEY = 1;//1表示私钥
 
 
    public RSAEncrypt() {
    }
 
    /**
     * 公钥加密
     * @param str 明文参数
     * @param publicKey 公钥
     * @return
     * @throws Exception
     */
    public static String encryptPublic(String str, String publicKey) throws Exception {
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey)KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        Cipher cipher = getCipher(1, pubKey);
        return splitEncrypt(str, cipher, pubKey.getModulus());
    }
    /**
     * 私钥加密
     * @param str 明文参数
     * @param privateKey 私钥
     * @return
     * @throws Exception
     */
    public static String encryptPrivate(String str, String privateKey) throws Exception {
        byte[] decoded = Base64.decodeBase64(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey)KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        Cipher cipher = getCipher(1, priKey);
        return splitEncrypt(str, cipher, priKey.getModulus());
    }
    /**
     * 公钥解密
     * @param str  密文参数
     * @param publicKey 公钥
     * @return
     * @throws Exception
     */
    public static String decryptPublic(String str, String publicKey) throws Exception {
        byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
        byte[] decoded = Base64.decodeBase64(publicKey);
        RSAPublicKey pubKey = (RSAPublicKey)KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
        Cipher cipher = getCipher(2, pubKey);
        return splitDecrypt(str, cipher, pubKey.getModulus());
    }
 
    /**
     * 私钥解密
     * @param str  密文参数
     * @param privateKey 私钥
     * @return
     * @throws Exception
     */
    public static String decryptPrivate(String str, String privateKey) throws Exception {
//        byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
        byte[] decoded = Base64.decodeBase64(privateKey);
        RSAPrivateKey priKey = (RSAPrivateKey)KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
        Cipher cipher = getCipher(2, priKey);
        return splitDecrypt(str, cipher, priKey.getModulus());
    }
 
    private static String splitDecrypt(String str, Cipher cipher, BigInteger modulus) throws Exception {
        byte[] bytes = Base64.decodeBase64(str);
        int inputLen = bytes.length;
        int offLen = 0;
        int i = 0;
 
        ByteArrayOutputStream byteArrayOutputStream;
        byte[] cache;
        for(byteArrayOutputStream = new ByteArrayOutputStream(); inputLen - offLen > 0; offLen = 128 * i) {
            if (inputLen - offLen > 128) {
                cache = cipher.doFinal(bytes, offLen, 128);
            } else {
                cache = cipher.doFinal(bytes, offLen, inputLen - offLen);
            }
 
            byteArrayOutputStream.write(cache);
            ++i;
        }
 
        byteArrayOutputStream.close();
        cache = byteArrayOutputStream.toByteArray();
        return new String(cache);
    }
 
    private static Cipher getCipher(int model, Key key) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(model, key);
        return cipher;
    }
 
    private static String splitEncrypt(String str, Cipher cipher, BigInteger modulus) throws Exception {
        byte[] bytes = str.getBytes();
        int inputLen = bytes.length;
        int offLen = 0;
        int i = 0;
 
        ByteArrayOutputStream bops;
        byte[] cache;
        for(bops = new ByteArrayOutputStream(); inputLen - offLen > 0; offLen = 117 * i) {
            if (inputLen - offLen > 117) {
                cache = cipher.doFinal(bytes, offLen, 117);
            } else {
                cache = cipher.doFinal(bytes, offLen, inputLen - offLen);
            }
 
            bops.write(cache);
            ++i;
        }
 
        bops.close();
        cache = bops.toByteArray();
        String encodeToString = Base64.encodeBase64String(cache);
        return encodeToString;
    }
 
    /**
     * 随机生成密钥对
     * @throws NoSuchAlgorithmException
     */
    public static void genKeyPair() throws NoSuchAlgorithmException {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(1024,new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   // 得到私钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  // 得到公钥
        String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
        // 得到私钥字符串
        String privateKeyString = new String(Base64.encodeBase64((privateKey.getEncoded())));
        publicKeyString="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCXOpuQMfJN2lC+8nKK05F6XBa49W0ReoKmnwmC9/bPizsm7P8t7yt6uwxFlswue8xmdY6HezssvVaUyq4DJrqPa5jR256uhWLSPF479uFJn4AsWhCj2JnPNk+C15m83A7YicN5tH7328c+wqyteRGyvWkXdxraX8ZE/MLqQ3KkCwIDAQAB";
        privateKeyString="MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJc6m5Ax8k3aUL7ycorTkXpcFrj1bRF6gqafCYL39s+LOybs/y3vK3q7DEWWzC57zGZ1jod7Oyy9VpTKrgMmuo9rmNHbnq6FYtI8Xjv24UmfgCxaEKPYmc82T4LXmbzcDtiJw3m0fvfbxz7CrK15EbK9aRd3GtpfxkT8wupDcqQLAgMBAAECgYA3wJqTSWab/w1fqABEYAU1B/g0jf58wB6kkOMvznzGPP08t4jIBXGRGJ5IlXSG+or7hwFZvzusnczqo+INNVz3a3ZG73y20AKwjY2ZOSa+0vpX8awZwoAZhk7cFSq95FGVDB08iMx4WXZ3bZCQPa9Hul5F5PAOL7PM0QTzKsoNQQJBANLKk6aQ+evFHPrVZOD1/LEGQpYYYowrCDhWjYSGhHX+NFGlSu19qZ9Bv4PlFufX2Qj767NhRc9cjcgFYkFoYCECQQC3qceEmSwJVKKH8Mu9iqtEWggnStUfMOjPEoV0vTcdsS6UYZsuMgsy5xKuuNzCq4lk2oO/gidLXJUMOa2E5K6rAkEAqBCmis7axVYDFidgaFEY6ZghLQVCRZnl0u2HphuOr3lAA7zzsclYncbRSPs2OSILRLN/qSUsgPm9fbZ1MctjoQJAFjkq9TZ7h52AFSbj4El5M8oZFngNlbqbUQMStu92Tdd0pWuyFULk7AHd1tpzi/Kpq1RtgKzFCG9pcZNBFcV+BQJAI46aiaQzZpkiLra5foBIzLM91BUK8E3Xa++UFpqiYO20sI6Hy1FHgRZUQ19IjZlW0O6ydTGwWTVmjvWIHBRAcQ==";
        // 将公钥和私钥保存到Map
        keyMap.put(PUBLICKEY ,publicKeyString); 
        keyMap.put(PRIVATEKEY ,privateKeyString); 
    }
 
    public static void main(String[] args) throws Exception {
        //参数
       // String str = "{\"client_id\":\"cmdb\",\"client_secret\":\"5sdKbV0xVzQb4Stw\",\"grant_type\":\"client_credential\"}";
        String str="{\"refresh_token\":\"1e7b03b9631f478e86e3c39f96144950\"}";
        System.out.println("参数:" + str);
        //随机生成密钥对
        genKeyPair();
        //公钥加密
        System.out.println("公钥:" + keyMap.get(PUBLICKEY));
        System.out.println("私钥:" + keyMap.get(PRIVATEKEY));
        String encrypt = RSAEncrypt.encryptPublic(str, keyMap.get(PUBLICKEY));
        System.out.println("公钥加密:" + encrypt);
        //私钥解密
        encrypt="hwUVmSTMJ4XuuggFbAOqk3VEzCkkYZRAz7S4wZtebd+XUC7VSW8CxY8q/Zv4mLIRNEs/CAMEk0SIGhhUM6fHe9FEol6/zXsx8O0xDx+GV2cO5M2nU3VGUocwnpua7KX/HGQXwhPScJWveaZSXUzuHtqDafnuxTQLCJVNgt+7kPqAQ84JfuZL7TuphFaA3MpsHKLOzrLnCi75xLrMW1/DuPkwSczMVG9uuxnvI2kDvv0bY95af9DcJtLYxgxHS4SkjcvJ6GwoKe+iLqrvu1buz0LQ8Z+q0ZUl/bsABJSETblC9qwvjI6QQQ1R2ZnvFG9Gav3W6EmU+UIFmSjWxVQQfQ==";
        String decrypt = RSAEncrypt.decryptPrivate(encrypt, keyMap.get(PRIVATEKEY));
        System.out.println("私钥解密:" + decrypt);
 
        //私钥加密
        String encryptPrivate = RSAEncrypt.encryptPrivate(str, keyMap.get(PRIVATEKEY));
        System.out.println("私钥加密:" + encryptPrivate);
        //公钥解密
        String decryptPublic = RSAEncrypt.decryptPublic(encryptPrivate, keyMap.get(PUBLICKEY));
        System.out.println("公钥解密:" + decryptPublic);
 
    }
}