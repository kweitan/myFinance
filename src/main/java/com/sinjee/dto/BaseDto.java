package com.sinjee.dto;

import lombok.Data;

/**
 * @author 小小极客
 * 时间 2019/10/14 23:22
 * @ClassName BaseDto
 * 描述 DTO基本类
 **/
@Data
public class BaseDto {

    //创建日期
    private String createDate ;

    //创建者
    private String createUser ;

    //最近更新日期
    private String lastUpdateDate ;
}
