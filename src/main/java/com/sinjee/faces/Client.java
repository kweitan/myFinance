package com.sinjee.faces;

import com.baidu.aip.face.AipFace;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * @author 小小极客
 * 时间 2019/11/7 23:42
 * @ClassName Client
 * 描述 TODO
 **/
public class Client {

    public static void main(String[] args) {
        // 初始化一个AipFace
        AipFace client = AipFaceUtil.getInstance().getAipFace() ;

        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("face_field", "age,gender,beauty,qualities");
        options.put("max_face_num", "2");
        options.put("face_type", "LIVE");
        options.put("liveness_control", "LOW");

        // 可选：设置网络连接参数
        client.setConnectionTimeoutInMillis(2000);
        client.setSocketTimeoutInMillis(60000);

        // 可选：设置代理服务器地址, http和socket二选一，或者均不设置
//        client.setHttpProxy("proxy_host", proxy_port);  // 设置http代理
//        client.setSocketProxy("proxy_host", proxy_port);  // 设置socket代理

        // 调用接口
        String image = "http://i1.zhiaigou.com/uploads/tu/201908/9999/8ff2d91a8d.jpg";
        String imageType = "URL";

        // 人脸检测
        JSONObject res = client.detect(image, imageType, options);
        System.out.println(res.toString(2));
        res.getJSONObject("result").getJSONObject("face_num").toString();
    }
}
