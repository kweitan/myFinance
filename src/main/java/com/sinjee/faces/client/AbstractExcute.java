package com.sinjee.faces.client;

import com.baidu.aip.face.AipFace;
import com.sinjee.faces.AipFaceDTO;
import com.sinjee.faces.AipFaceUtil;

import java.util.HashMap;

/**
 * 创建时间 2019 - 11 -08
 *
 * @author kweitan
 */
public abstract class AbstractExcute {


    public void start(AipFace client,String image,String imageType){
        getAipFaceDTO(client,image,imageType);
    }

    protected abstract AipFaceDTO getAipFaceDTO(AipFace client,String image,String imageType) ;
}
