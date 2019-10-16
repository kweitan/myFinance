import com.sinjee.dto.MongoTestDto;
import com.sinjee.entity.MongoTestEntity;
import com.sinjee.tools.BeanConversionUtils;
import com.sinjee.tools.RSAUtil;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

import java.security.KeyPair;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName TestLists
 * @Description TODO
 * @Author pc
 * @Date 2019/10/6 9:43
 * @Version 1.0
 **/
public class TestLists {

    @Test
    public void copyLists(){
        System.out.println("test");
        MongoTestEntity source = new MongoTestEntity() ;
        source.setId(200);
        source.setUserName("kweitan");
        source.setAge("20");

        MongoTestEntity source1 = new MongoTestEntity() ;
        source1.setId(201);
        source1.setUserName("guixin");
        source1.setAge("28");

        List<MongoTestEntity> sourceList = new ArrayList<>() ;
        sourceList.add(source);
        sourceList.add(source1);

        List<MongoTestDto> destList = new ArrayList<>() ;
//        BeanConversionUtils.listCopyToAnotherList(sourceList,destList);
    }

    @Test
    public void testRSA(){
        try{
            // 生成密钥对
            KeyPair keyPair = RSAUtil.getKeyPair();
            String privateKey = new String(Base64.encodeBase64(keyPair.getPrivate().getEncoded()));
            String publicKey = new String(Base64.encodeBase64(keyPair.getPublic().getEncoded()));
            System.out.println("私钥:" + privateKey);
            System.out.println("公钥:" + publicKey);
            // RSA加密
            String data = "您好啊 RSA!";
            System.out.println("加密前内容:" + data);
            //开始计时
            long startTime = System.currentTimeMillis() ;
            List<String> list = new ArrayList<>() ;
            for(int i=0; i<10000;i++){
                String encryptData = RSAUtil.encrypt(data+":"+i, RSAUtil.getPublicKey(publicKey));
                list.add(encryptData) ;
                System.out.println("加密后内容:" + encryptData);
            }

            //10000条数据加密消耗时间
            long encryptTime = System.currentTimeMillis() - startTime ;

            startTime = System.currentTimeMillis() ;
            // RSA解密
            for(String content:list){
                String decryptData = RSAUtil.decrypt(content, RSAUtil.getPrivateKey(privateKey));
                System.out.println("解密后内容:" + decryptData);
            }
            //10000条数据解密消耗时间
            long decryptTime = System.currentTimeMillis() - startTime ;

            System.out.println("10000条数据加密消耗时间："+
                    (encryptTime)+" 毫秒！");
            System.out.println("10000条数据解密消耗时间："+
                    (decryptTime)+" 毫秒！");

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void testRSA1024(){
        try{
            // 生成密钥对
            KeyPair keyPair = RSAUtil.getKeyPair();
            String privateKey = new String(Base64.encodeBase64(keyPair.getPrivate().getEncoded()));
            String publicKey = new String(Base64.encodeBase64(keyPair.getPublic().getEncoded()));
            System.out.println("私钥:" + privateKey);
            System.out.println("公钥:" + publicKey);
            // RSA加密
            String data = "您好啊 RSA!";
            System.out.println("加密前内容:" + data);
            String encryptData = RSAUtil.encrypt(data, RSAUtil.getPublicKey(publicKey));
            System.out.println("加密后内容:" + encryptData);
            // RSA解密
            String decryptData = RSAUtil.decrypt(encryptData, RSAUtil.getPrivateKey(privateKey));
            System.out.println("解密后内容:" + decryptData);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
