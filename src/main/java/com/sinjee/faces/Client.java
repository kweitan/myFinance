package com.sinjee.faces;

import com.baidu.aip.face.AipFace;
import com.sinjee.faces.common.GsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * @author 小小极客
 * 时间 2019/11/7 23:42
 * @ClassName Client
 * 描述 客户端
 **/
@Slf4j
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

        // 调用接口
        String image = "http://i1.zhiaigou.com/uploads/tu/201908/9999/8ff2d91a8d.jpg";
        String imageType = "URL";

        // 人脸检测
        JSONObject res = client.detect(image, imageType, options);
        System.out.println(res.toString(2));

        //先判断是否成功
        if (null != res && res.getString("error_msg").equals("SUCCESS")){
            String result = res.getJSONObject("result").toString();
            AipFaceDTO aipFaceDTO = GsonUtil.json2Bean(result,AipFaceDTO.class);
            log.info(aipFaceDTO.toString());
        }else{
            log.error("识别失败");
        }
    }
}
