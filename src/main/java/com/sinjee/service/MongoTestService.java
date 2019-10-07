package com.sinjee.service;

import com.sinjee.dto.MongoTestDto;
import com.sinjee.tools.PageModel;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MongoTestService {
    /**
     * 保存对象
     * @param mongoTestDto
     * @return
     */
    public MongoTestDto saveMongoTest(MongoTestDto mongoTestDto) ;

    /**
     * 更新对象
     * @param mongoTestDto
     * @return
     */
    public long updateMongoTest(MongoTestDto mongoTestDto);

    /**
     * 删除对象
     * @param id
     * @return
     */
    public long deleteMongoTest(Integer id);

    /**
     * 根据name查找对象
     * @param name
     * @return
     */
    public List<MongoTestDto> findMongoTestByName(String name);

    /**
     * 根据age查找列表
     * @param age
     * @return
     */
    public List<MongoTestDto> findMongoTestByAge(String age);

    /**
     * 根据ID查找对象
     * @param id
     * @return
     */
    public MongoTestDto findMongoTestById(Integer id);

    /**
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    public Page<MongoTestDto> findAllByPageable(int page, int size);
}
