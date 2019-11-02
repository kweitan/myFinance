package com.sinjee.service.impl;

import com.sinjee.dto.ZhangTingAnalysisDto;
import com.sinjee.service.ZhangTingAnalysisService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author 小小极客
 * 时间 2019/10/19 23:42
 * ZhangTingAnalysisServiceImplTest
 * 描述 Service测试类
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class ZhangTingAnalysisServiceImplTest {

    @Autowired
    private ZhangTingAnalysisService zhangTingAnalysisService ;

    @Test
    public void testQuery(){
        Page<ZhangTingAnalysisDto> page = zhangTingAnalysisService.getPageByTradeDate(20191008,2,0,10) ;
        log.info(page.getContent().toString());
    }
}
