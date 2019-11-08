package com.sinjee.faces;

/**
 * @author 小小极客
 * 时间 2019/11/8 0:03
 * @ClassName FaceListDTO
 * 描述 人脸信息列表，具体包含的参数参考下面的列表。
 **/
public class FaceListDTO {

    private String face_token ; //人脸图片的唯一标识

    private LocationDTO location ; //人脸在图片中的位置

    private Gender gender ; //性别，face_field包含gender时返回

    private double beauty ; //美丑打分，范围0-100，越大表示越美。当face_fields包含beauty时返回

    private double age ; //年龄


}
