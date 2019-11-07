package com.sinjee.faces;

import com.baidu.aip.face.AipFace;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * @author 小小极客
 * 时间 2019/11/7 22:51
 * @ClassName AipFaceUtil
 * 描述 AipFace是人脸识别的Java客户端，为使用人脸识别的开发人员提供了一系列的交互方法。
 **/
public class AipFaceUtil {

    //设置你的APPID/AK/SK(自己到百度人工智能平台上注册)
    private static final String APP_ID = "17720950";
    private static final String API_KEY = "25tEq8YOadkZrVYKri3XkWHw";
    private static final String SECRET_KEY = "ZmsktVfDwukYw9MKGSVqZPs39G1Yrm6l";

    private static final AipFaceUtil INSTANCE = new AipFaceUtil() ;

    private AipFace aipFace = null ;

    private AipFaceUtil(){
        // 初始化一个AipFace
        this.aipFace = new AipFace(APP_ID, API_KEY, SECRET_KEY);
    }

    public AipFace getAipFace(){
        return this.aipFace ;
    }

    public synchronized static AipFaceUtil getInstance(){    return INSTANCE;  }
}
