package com.sinjee.tools;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/****
 * RSA加密、解密、签名、验签的原理及方法
 * 一、RSA加密简介
 *
 * RSA加密是一种非对称加密。可以在不直接传递密钥的情况下，完成解密。这能够确保信息的安全性，
 * 避免了直接传递密钥所造成的被破解的风险。是由一对密钥来进行加解密的过程，分别称为公钥和私钥。
 * 两者之间有数学相关，该加密算法的原理就是对一极大整数做因数分解的困难性来保证安全性。
 * 通常个人保存私钥，公钥是公开的（可能同时多人持有）。
 *
 * 二、RSA加密、签名区别
 * 加密和签名都是为了安全性考虑，但略有不同。常有人问加密和签名是用私钥还是公钥？其实都是对加密和签名的作用有所混淆。
 * 简单的说，加密是为了防止信息被泄露，而签名是为了防止信息被篡改。这里举2个例子说明。
 *
 * 第一个场景：战场上，B要给A传递一条消息，内容为某一指令。
 *
 * RSA的加密过程如下：
 *
 * （1）A生成一对密钥（公钥和私钥），私钥不公开，A自己保留。公钥为公开的，任何人可以获取。
 *
 * （2）A传递自己的公钥给B，B用A的公钥对消息进行加密。
 *
 * （3）A接收到B加密的消息，利用A自己的私钥对消息进行解密。
 *
 * 　　在这个过程中，只有2次传递过程，第一次是A传递公钥给B，第二次是B传递加密消息给A，即使都被敌方截获，
 * 也没有危险性，因为只有A的私钥才能对消息进行解密，防止了消息内容的泄露。
 *
 * 第二个场景：A收到B发的消息后，需要进行回复“收到”。
 *
 * RSA签名的过程如下：
 *
 * （1）A生成一对密钥（公钥和私钥），私钥不公开，A自己保留。公钥为公开的，任何人可以获取。
 *
 * （2）A用自己的私钥对消息加签，形成签名，并将加签的消息和消息本身一起传递给B。
 *
 * （3）B收到消息后，在获取A的公钥进行验签，如果验签出来的内容与消息本身一致，证明消息是A回复的。
 *
 * 　　在这个过程中，只有2次传递过程，第一次是A传递加签的消息和消息本身给B，第二次是B获取A的公钥，
 * 即使都被敌方截获，也没有危险性，因为只有A的私钥才能对消息进行签名，即使知道了消息内容，
 * 也无法伪造带签名的回复给B，防止了消息内容的篡改。
 *
 * 但是，综合两个场景你会发现，第一个场景虽然被截获的消息没有泄露，但是可以利用截获的公钥，
 * 将假指令进行加密，然后传递给A。第二个场景虽然截获的消息不能被篡改，但是消息的内容可以利用公钥验签来获得，
 * 并不能防止泄露。所以在实际应用中，要根据情况使用，也可以同时使用加密和签名，
 * 比如A和B都有一套自己的公钥和私钥，当A要给B发送消息时，先用B的公钥对消息加密，
 * 再对加密的消息使用A的私钥加签名，达到既不泄露也不被篡改，更能保证消息的安全性。
 *
 * 总结：公钥加密、私钥解密、私钥签名、公钥验签。
 *
 * PS:RSA加密对明文的长度有所限制，规定需加密的明文最大长度=密钥长度-11（单位是字节，即byte），
 * 所以在加密和解密的过程中需要分块进行。而密钥默认是1024位，即1024位/8位-11=128-11=117字节。
 * 所以默认加密前的明文最大长度117字节，解密密文最大长度为128字。那么为啥两者相差11字节呢？
 * 是因为RSA加密使用到了填充模式（padding），即内容不足117字节时会自动填满，
 * 用到填充模式自然会占用一定的字节，而且这部分字节也是参与加密的。
 *
 * 密钥长度的设置就是上面例子的第32行。可自行调整，当然非对称加密随着密钥变长，安全性上升的同时性能也会有所下降。
 */
public class RSAUtil {
    /**
     * RSA最大加密明文大小
     * 1024/8-11 或者 2048/8-11
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * 单向散列加密SHA256
     */
    private static final String SHA256 = "SHA256withRSA" ;

    /**
     * 单向散列加密MD5
     */
    private static final String MD5 = "MD5withRSA" ;

    /**
     * 加密长度 RSA 2048 1024
     */
    private static final int MAX_LENGTH = 1024 ;

    /**
     * 非对称加密算法
     */
    private static final String ALGORITHM_TYPE = "RSA" ;

    /**
     * 获取密钥对
     *
     * @return 密钥对
     */
    public static KeyPair getKeyPair() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance(ALGORITHM_TYPE);
        generator.initialize(MAX_LENGTH);
        return generator.generateKeyPair();
    }

    /**
     * 获取私钥
     *
     * @param privateKey 私钥字符串
     * @return
     */
    public static PrivateKey getPrivateKey(String privateKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_TYPE);
        byte[] decodedKey = Base64.decodeBase64(privateKey.getBytes());
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(decodedKey);
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 获取公钥
     *
     * @param publicKey 公钥字符串
     * @return
     */
    public static PublicKey getPublicKey(String publicKey) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_TYPE);
        byte[] decodedKey = Base64.decodeBase64(publicKey.getBytes());
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * RSA加密
     *
     * @param data 待加密数据
     * @param publicKey 公钥
     * @return
     */
    public static String encrypt(String data, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM_TYPE);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        int inputLen = data.getBytes().length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段加密
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_ENCRYPT_BLOCK) {
                cache = cipher.doFinal(data.getBytes(), offset, MAX_ENCRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(data.getBytes(), offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_ENCRYPT_BLOCK;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        // 获取加密内容使用base64进行编码,并以UTF-8为标准转化成字符串
        // 加密后的字符串
        return new String(Base64.encodeBase64String(encryptedData));
    }

    /**
     * RSA解密
     *
     * @param data 待解密数据
     * @param privateKey 私钥
     * @return
     */
    public static String decrypt(String data, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM_TYPE);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] dataBytes = Base64.decodeBase64(data);
        int inputLen = dataBytes.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offset = 0;
        byte[] cache;
        int i = 0;
        // 对数据分段解密
        while (inputLen - offset > 0) {
            if (inputLen - offset > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(dataBytes, offset, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(dataBytes, offset, inputLen - offset);
            }
            out.write(cache, 0, cache.length);
            i++;
            offset = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        // 解密后的内容
        return new String(decryptedData, "UTF-8");
    }

    /**
     * 签名
     *
     * @param data 待签名数据
     * @param privateKey 私钥
     * @return 签名
     */
    public static String sign(String data, PrivateKey privateKey) throws Exception {
        byte[] keyBytes = privateKey.getEncoded();
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_TYPE);
        PrivateKey key = keyFactory.generatePrivate(keySpec);
        Signature signature = Signature.getInstance(SHA256);
        signature.initSign(key);
        signature.update(data.getBytes());
        return new String(Base64.encodeBase64(signature.sign()));
    }

    /**
     * 验签
     *
     * @param srcData 原始字符串
     * @param publicKey 公钥
     * @param sign 签名
     * @return 是否验签通过
     */
    public static boolean verify(String srcData, PublicKey publicKey, String sign) throws Exception {
        byte[] keyBytes = publicKey.getEncoded();
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM_TYPE);
        PublicKey key = keyFactory.generatePublic(keySpec);
        Signature signature = Signature.getInstance(SHA256);
        signature.initVerify(key);
        signature.update(srcData.getBytes());
        return signature.verify(Base64.decodeBase64(sign.getBytes()));
    }

    public static void main(String[] args) {
        try {
            // 生成密钥对
            KeyPair keyPair = getKeyPair();
            String privateKey = new String(Base64.encodeBase64(keyPair.getPrivate().getEncoded()));
            String publicKey = new String(Base64.encodeBase64(keyPair.getPublic().getEncoded()));
            System.out.println("私钥:" + privateKey);
            System.out.println("公钥:" + publicKey);
            // RSA加密
            String data = "您好啊 RSA!";
            System.out.println("加密前内容:" + data);
            String encryptData = encrypt(data, getPublicKey(publicKey));
            System.out.println("加密后内容:" + encryptData);
            // RSA解密
            String decryptData = decrypt(encryptData, getPrivateKey(privateKey));
            System.out.println("解密后内容:" + decryptData);

            // RSA签名
            String sign = sign(data, getPrivateKey(privateKey));
            // RSA验签
            boolean result = verify(data, getPublicKey(publicKey), sign);
            System.out.print("验签结果:" + result);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.print("加解密异常");
        }
    }
}
