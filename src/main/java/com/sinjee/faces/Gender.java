package com.sinjee.faces;

import lombok.Data;

/**
 * @author 小小极客
 * 时间 2019/11/8 0:08
 * @ClassName Gender
 * 描述 性别，face_field包含gender时返回
 **/
@Data
public class Gender {
    private String type ; //male:男性 female:女性
    private double probability ; //性别置信度，范围【0~1】，0代表概率最小、1代表最大。
}
