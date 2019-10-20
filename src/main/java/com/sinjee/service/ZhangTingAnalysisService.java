package com.sinjee.service;

import com.sinjee.dto.ZhangTingAnalysisDto;
import org.springframework.data.domain.Page;

public interface ZhangTingAnalysisService {

    /**
     * 根据交易日期 连扳数 获取数据
     * @param tradeDate
     * @param lianbanNum
     * @param page
     * @param size
     * @return
     */
    public Page<ZhangTingAnalysisDto> getPageByTradeDate(int tradeDate, int zhangtingNum, int page, int size) ;
}
