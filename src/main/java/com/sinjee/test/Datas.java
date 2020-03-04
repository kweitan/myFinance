package com.sinjee.test;

import lombok.Data;

import java.util.List;

/**
 * @author 小小极客
 * 时间 2020/3/5 0:30
 * @ClassName Datas
 * 描述 Datas
 **/
@Data
public class Datas {

    private Integer ret ;

    private String info ;

    private List<CountryVO> data ;
}
