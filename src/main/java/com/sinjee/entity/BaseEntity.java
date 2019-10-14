package com.sinjee.entity;

import lombok.Data;

/**
 * @author 小小极客
 * 时间 2019/10/14 23:23
 * @ClassName BaseEntity
 * 描述 BaseEntity基础类
 **/
@Data
public class BaseEntity {

    //创建日期
    private String createDate ;

    //创建者
    private String createUser ;

    //最近更新日期
    private String lastUpdateDate ;
}
