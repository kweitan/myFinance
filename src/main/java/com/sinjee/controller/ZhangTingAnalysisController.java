package com.sinjee.controller;

import com.sinjee.dto.ZhangTingAnalysisDto;
import com.sinjee.service.ZhangTingAnalysisService;
import com.sinjee.vo.ResultVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author 小小极客
 * 时间 2019/10/19 15:57
 * @ClassName ZhangTingAnalysisController
 * 描述 ZhangTingAnalysis 控制层处理逻辑
 **/
@RestController
@RequestMapping("/ZhangTingAnalysis")
@Slf4j
public class ZhangTingAnalysisController {

    @Autowired
    private ZhangTingAnalysisService zhangTingAnalysisService ;

    @GetMapping("/list")
    public ResultVo<List<ZhangTingAnalysisDto>> list(@RequestParam(value = "tradeDate") Integer tradeDate,
                                                    @RequestParam(value = "zhangtingNum") Integer zhangtingNum,
                                                    @RequestParam(value = "page", defaultValue = "0") Integer page,
                                                     @RequestParam(value = "size", defaultValue = "10") Integer size){
        Page<ZhangTingAnalysisDto> pageImpl = zhangTingAnalysisService.getPageByTradeDate(tradeDate,zhangtingNum,page,size) ;
        ResultVo<List<ZhangTingAnalysisDto>> result = new ResultVo<List<ZhangTingAnalysisDto>>() ;
        result.setCode(200);
        result.setMsg("成功");
        result.setData(pageImpl.getContent());
        return result ;
    }
}
