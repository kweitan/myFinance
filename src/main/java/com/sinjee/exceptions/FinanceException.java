package com.sinjee.exceptions;

import com.sinjee.enums.ResultEnum;
import lombok.Getter;

/**
 * @ClassName FinanceException
 * @Description 统一异常处理
 * @Author pc
 * @Date 2019/10/6 16:12
 * @Version 1.0
 **/
@Getter
public class FinanceException extends RuntimeException {
    private Integer code ;

    public FinanceException(ResultEnum resultEnum){
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode() ;
    }

    public FinanceException(Integer code,String message){
        super(message);
        this.code = code ;
    }
}
