package com.sinjee.handler;

import com.sinjee.exceptions.FinanceException;
import com.sinjee.tools.ResultVoUtil;
import com.sinjee.vo.ResultVo;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName FinanceExceptionHandler
 * @Description 统一异常处理
 * @Author pc
 * @Date 2019/10/6 16:21
 * @Version 1.0
 **/
@ControllerAdvice
public class FinanceExceptionHandler {

    /**
     * 拦截异常处理
     * @return
     */
    @ExceptionHandler(value = FinanceException.class)
    @ResponseBody
    public ResultVo handlerFinanceException(FinanceException e){
        return ResultVoUtil.error(e.getCode(),e.getMessage());
    }
}
