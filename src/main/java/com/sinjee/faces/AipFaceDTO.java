package com.sinjee.faces;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author 小小极客
 * 时间 2019/11/8 0:00
 * @ClassName AipFaceDTO
 * 描述 人脸检测 返回数据参数详情
 **/
@Slf4j
@Data
public class AipFaceDTO {

    private Integer face_num ; //检测到的图片中的人脸数量

    private List<FaceListDTO> face_list ;

}
