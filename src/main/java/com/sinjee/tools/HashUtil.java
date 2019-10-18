package com.sinjee.tools;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;

/**
 * 单向散列算法(指纹算法，以防止篡改)
 * 不推荐使用(MD5 SHA1)
 * 推荐使用SHA256
 */
@Slf4j
public class HashUtil {

    /**
     * 签名字符
     * @param text 签名的字符串
     * @param key 密钥
     * @param charset 编码格式
     * @return 签名结果
     */
    public static String sign(String text, String key, String charset) {
        text = text + key;
        return DigestUtils.sha256Hex(getContentBytes(text, charset));
    }

    /**
     * 签名的字符串
     * @param text
     * @param charset
     * @return
     * @throws Exception
     */
    public static String signKey(String text,String charset){
        String result = null ;
        try {
            result = Base64.encodeBase64String(DigestUtils.sha256(text.getBytes(charset)));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result ;
    }

    /**
     * 签名字符
     * @param text 签名的字符串
     * @param sign 签名结果
     * @param key 密钥
     * @param charset 编码格式
     * @return 签名结果
     */
    public static boolean verify(String text, String key,String charset,String sign) {
        text = text + key;
        String mysign = DigestUtils.sha256Hex(getContentBytes(text, charset));
        if(mysign.equals(sign)) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * @param content
     * @param charset
     * @return
     */
    private static byte[] getContentBytes(String content, String charset) {
        if (charset == null || "".equals(charset)) {
            return content.getBytes();
        }
        try {
            return content.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("SHA256签名过程中出现错 指定的编码集不对,您目前指定的编码集是:" + charset);
        }
    }

    public static void main(String[] args) {
        String text = "Hello SHA256!" ;
        String key = "are you ok" ;
        //签名
        String sign = sign(text,key,"UTF-8") ;
        log.info("签名字符串：{}",sign);
        log.info("签名验证结果：{}",verify(text,key,"UTF-8",sign));
    }
}
