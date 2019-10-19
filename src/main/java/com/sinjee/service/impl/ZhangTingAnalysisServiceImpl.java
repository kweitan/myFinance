package com.sinjee.service.impl;

import com.sinjee.dao.ZhangTingAnalysisDao;
import com.sinjee.dto.ZhangTingAnalysisDto;
import com.sinjee.entity.ZhangTingAnalysisEntity;
import com.sinjee.service.ZhangTingAnalysisService;
import com.sinjee.tools.BeanConversionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 小小极客
 * 时间 2019/10/19 15:51
 * @ClassName ZhangTingAnalysisServiceImpl
 * 描述 TODO
 **/
@Slf4j
@Service
public class ZhangTingAnalysisServiceImpl implements ZhangTingAnalysisService {

    @Autowired
    private ZhangTingAnalysisDao zhangTingAnalysisDao ;

    @Override
    public Page<ZhangTingAnalysisDto> getPageByTradeDate(String tradeDate, int lianbanNum, int page, int size) {
        Page<ZhangTingAnalysisEntity> pageModel = zhangTingAnalysisDao.getPageByTradeDate(tradeDate,lianbanNum,page,size) ;
        List<ZhangTingAnalysisDto> dtoList = null ;
        if (null != pageModel){
            dtoList = BeanConversionUtils.listCopyToAnotherList(ZhangTingAnalysisDto.class,pageModel.getContent()) ;
        }

        return new PageImpl(dtoList,pageModel.getPageable(),pageModel.getTotalElements());
    }
}
