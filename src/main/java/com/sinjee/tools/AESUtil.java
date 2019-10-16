package com.sinjee.tools;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * AES对称加密工具类
 */
@Slf4j
public class AESUtil{

    private static final String KEY_ALGORITHM = "AES";

    /**
     * 算法/模组/补码
     */
    private static final String DEFAULT_CIPHER_ALGORITHM = "AES/GCM/PKCS5Padding"; // 默认的加密算法

    /**
     * AES 加密操作
     *
     * @param content     待加密内容
     * @param encryptPass 加密密码
     * @return 返回Base64转码后的加密数据
     */
    public static String encrypt(String content, String encryptPass) {
        try {
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(encryptPass));
            byte[] iv = cipher.getIV();
            assert iv.length == 12;
            byte[] encryptData = cipher.doFinal(content.getBytes());
            assert encryptData.length == content.getBytes().length + 16;
            byte[] message = new byte[12 + content.getBytes().length + 16];
            System.arraycopy(iv, 0, message, 0, 12);
            System.arraycopy(encryptData, 0, message, 12, encryptData.length);
            return Base64.encodeBase64String(message);
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
                | BadPaddingException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * AES 解密操作
     *
     * @param base64Content
     * @param encryptPass
     * @return
     */
    public static String decrypt(String base64Content, String encryptPass) {
        byte[] content = Base64.decodeBase64(base64Content);
        if (content.length < 12 + 16)
            throw new IllegalArgumentException();
        GCMParameterSpec params = new GCMParameterSpec(128, content, 0, 12);
        try {
            Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, getSecretKey(encryptPass), params);
            byte[] decryptData = cipher.doFinal(content, 12, content.length - 12);
            return new String(decryptData);
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException
                | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 生成加密秘钥
     *
     * @return
     * @throws NoSuchAlgorithmException
     */
    private static SecretKeySpec getSecretKey(String encryptPass) throws NoSuchAlgorithmException {
        KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
        // 初始化密钥生成器，AES要求密钥长度为128位、192位、256位
        kg.init(128, new SecureRandom(encryptPass.getBytes()));
        SecretKey secretKey = kg.generateKey();
        return new SecretKeySpec(secretKey.getEncoded(), KEY_ALGORITHM);// 转换为AES专用密钥
    }

    public static void main(String[] args) {
        String s = "您好啊 AES";
        String pass = "EBD0B5D2-F4E7-453A-9E16-01AE8E2977";
        String encoded = encrypt(s, pass);
        log.info("加密之前：{}", s);
        log.info("加密结果：{}", encoded);
        log.info("解密结果：{}", decrypt(encoded, pass));
    }
}


