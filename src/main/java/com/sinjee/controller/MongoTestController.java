package com.sinjee.controller;

import com.sinjee.dto.MongoTestDto;
import com.sinjee.service.MongoTestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * @ClassName MongoTestController
 * @Description MongoTestController控制层
 * @Author pc
 * @Date 2019/10/6 16:32
 * @Version 1.0
 **/
@Controller
@RequestMapping("/MongoTest")
@Slf4j
public class MongoTestController {

    @Autowired
    private MongoTestService mongoTestService ;

    /**
     * 查询列表
     * @param page
     * @param size
     * @return
     */
//    @GetMapping("/listVo")
//    public ResultVo<List<MongoTestDto>> listVo(@RequestParam(value = "page", defaultValue = "0") Integer page,
//                                             @RequestParam(value = "size", defaultValue = "10") Integer size) {
//        PageModel<MongoTestDto> pageModel = mongoTestService.findAllByPageable(page,size);
//        return ResultVoUtil.success(pageModel.getDatas());
//    }

    /**
     * 查询列表
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/list")
    public ModelAndView list(@RequestParam(value = "page", defaultValue = "1") Integer page,
                             @RequestParam(value = "size", defaultValue = "3") Integer size,
                             Map<String, Object> map) {
        Page<MongoTestDto> pageModel = mongoTestService.findAllByPageable(page-1,size);
        map.put("MongoTestDtoList",pageModel.getContent());
        map.put("currentPage",page);
        map.put("totalPage",pageModel.getTotalPages());
        map.put("size",size) ;
        ModelAndView modelAndView = new ModelAndView("test/mongoTest", map);
        return modelAndView ;
    }
}
