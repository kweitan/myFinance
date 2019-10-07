package com.sinjee.tools;

import com.sinjee.vo.ResultVo;

/**
 * @ClassName ResultVoUtil
 * @Description ResultVo处理工具类
 * @Author pc
 * @Date 2019/10/6 16:29
 * @Version 1.0
 **/
public class ResultVoUtil {

    public static ResultVo success(Object object) {
        ResultVo resultVO = new ResultVo();
        resultVO.setData(object);
        resultVO.setCode(0);
        resultVO.setMsg("成功");
        return resultVO;
    }

    public static ResultVo success() {
        return success(null);
    }

    public static ResultVo error(Integer code, String msg) {
        ResultVo resultVO = new ResultVo();
        resultVO.setCode(code);
        resultVO.setMsg(msg);
        return resultVO;
    }

}
