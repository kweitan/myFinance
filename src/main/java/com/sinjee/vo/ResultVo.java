package com.sinjee.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName ResultVo
 * @Description http请求返回对象
 * @Author pc
 * @Date 2019/10/6 16:26
 * @Version 1.0
 **/
@Data
public class ResultVo<T> implements Serializable {
    /** 错误码. */
    private Integer code;

    /** 提示信息. */
    private String msg;

    /** 具体内容. */
    private T data;
}
