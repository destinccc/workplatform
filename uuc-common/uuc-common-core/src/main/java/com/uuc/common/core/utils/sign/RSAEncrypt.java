package com.uuc.common.core.utils.sign;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

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
        byte[] inputByte = Base64.decodeBase64(str.getBytes("UTF-8"));
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
        Cipher cipher = Cipher.getInstance("RSA");
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
        publicKeyString="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCegm9gJOgdJI0JHEa62BB2BomPi2IDUbLstremX4QS9egQpPKlGdix6yjngd3OzwTw7fpTSQlD27/m0jq9RE/yRAQwomYZPoCuxfZGJ7nAR49nUOYMKytCUjC8L8t2r6VqN1DowJK7vMrhkPXmbFdMSaegKwBv6al1+cWm2kWkCwIDAQAB";
        privateKeyString="MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJ6Cb2Ak6B0kjQkcRrrYEHYGiY+LYgNRsuy2t6ZfhBL16BCk8qUZ2LHrKOeB3c7PBPDt+lNJCUPbv+bSOr1ET/JEBDCiZhk+gK7F9kYnucBHj2dQ5gwrK0JSMLwvy3avpWo3UOjAkru8yuGQ9eZsV0xJp6ArAG/pqXX5xabaRaQLAgMBAAECgYATWfdA9KCF1k2FnDciXnh8AEZRCQI5R4MhlRkMrcKREgZ36btZ3rLu3bfLs7YhpnKC+3rBKjyAVVazVl0neCyORGuYLKNeeRX0ORCoo/v/kMyQ6GLWqvMFiDxZ3Bfy5V9XVKQ/3Dv7owbukCcMiNxHU8Gcm8ngrVVTqpp0QY9NWQJBAOsPkddKpTxQmIlDudV9OJwVXDfFbwB4nlbGteztkFIA3qDjYsCaU8eVYw5/1bz6OVUxltMNSMkSVXfDqkjDKR8CQQCsoSfbWASdrLvAdfUGCJU161GODITK6Wb9Tj+2N+/buz1sA7qFlzTT8mljubb4m7+KTODCg0/Z+ZxVqE5p6auVAkEAkjYcFFuVDkBfsToUVMxQMx5CP1qqohNlAjsWHGrk021BDbiCy6TzKfktJW60x5ipoul2lI710ER8L0PhHuNuJwJASHf+u13f0jFJQCpEihvgR5pZykAHHweMbJoEtQIWdy/LQSTvkFVBoBgLlgnIfeUkK3LKh92IEAFxV6QoBt5JYQJAFHpogk5gqT6HjZOGrmZc9QzkmD8NE5BSh6Mk7tMJfqkn3/vuq8tbOEXvZRLK0+BnexydSHfG+54X35vt4X2jPw==";
        // 将公钥和私钥保存到Map
        keyMap.put(PUBLICKEY ,publicKeyString); 
        keyMap.put(PRIVATEKEY ,privateKeyString); 
    }
 
    public static void main(String[] args) throws Exception {
        //参数
        String str = "{\"code\":\"326b65c5-52ea-4bd0-8a3d-554fa5217c0f\"}";
        System.out.println("参数:" + str);
        //随机生成密钥对
        genKeyPair();
        //公钥加密
        System.out.println("公钥:" + keyMap.get(PUBLICKEY));
        System.out.println("私钥:" + keyMap.get(PRIVATEKEY));
        String encrypt = RSAEncrypt.encryptPublic(str, keyMap.get(PUBLICKEY));
        System.out.println("公钥加密:" + encrypt);
        //私钥解密
        //encrypt="LfLao85+kduzTLuDwXcFeZgbrPCmXNj681LffbKMdYVnF10vbQcNytMwpRWwUkdj+X+5p0aPj+7A6286yWQD0RdLVSLpJC5KAXn7KyD24EfOdQYbZQHe1l6XowXwnM7EBgPA9NhXF7DCn/b6o/d52aF6Q5qHkw/13WEpLtf1JusVaLrtIkLfLy363mVOGTsgIGrZzCPrKyilzA/b/hCcggGW2f690GtaFPRp6LKrcv0MRQTRuHkHpTHVnF7tBny/hIsBVxOiONnnPXruQ0n+oEJzGc9EOuCXhYd446AuquYXu0WHhZaIrW4LlHePtvYT7V9rPGO1M4lRew0TZ85+iiUtrWOspy1F8T7T6DCvr3uoNZMBocw3df4AympPbFD5Szp8/d9Du3keL5puXEYf7eGVLZHFiUHK4d8yQCJyDWtwV7G6AWkMS8oJoS14KbmVZiBkgWYeDHhXwzX/IC9P+evhB+iscpbGGEar1IHt2qC3DdvsLuIkW6I4ZQfTKXNC";
        String decrypt = RSAEncrypt.decryptPrivate(encrypt, keyMap.get(PRIVATEKEY));
        System.out.println("私钥解密:" + decrypt);
 
        //私钥加密
        String encryptPrivate = RSAEncrypt.encryptPrivate(str, keyMap.get(PRIVATEKEY));
        System.out.println("私钥加密:" + encryptPrivate);
        //公钥解密
        encryptPrivate="cmqy8s8TszYmXmOE2OJ3BWnfp7Qwk9Sjh7zFPkolVh95KBuW6P7gHlhXnhHVFsmm7B5EIpVzOKI3lQP5mAfi7DzM7h6cwz/vVRESnz04J9aQF2Ka4jWVAvaKvyBlpdFEIIcEaf+8q+aZDzhqKIP3+J9IUMZbqtSx8fjvQl+Ap2w=";
        String decryptPublic = RSAEncrypt.decryptPublic(encryptPrivate, keyMap.get(PUBLICKEY));
        System.out.println("公钥解密:" + decryptPublic);
 
    }
}