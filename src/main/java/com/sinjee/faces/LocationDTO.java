package com.sinjee.faces;

import lombok.Data;

/**
 * @author 小小极客
 * 时间 2019/11/8 0:05
 * @ClassName LocationDTO
 * 描述 人脸在图片中的位置
 **/
@Data
public class LocationDTO {
    private double left ; //人脸区域离左边界的距离
    private double width ; //人脸区域的宽度
    private double top ; //人脸区域离上边界的距离
    private double height ; //人脸区域的高度
    private Integer rotation ; //人脸框相对于竖直方向的顺时针旋转角，[-180,180]
}
