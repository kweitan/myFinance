package com.sinjee.service.impl;

import com.sinjee.dto.MongoTestDto;
import com.sinjee.entity.MongoTestEntity;
import com.sinjee.service.MongoTestService;
import com.sinjee.tools.PageModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MongoTestServiceImplTest {

    @Autowired
    private MongoTestService mongoTestService ;

    @Test
    public void saveMongoTest() {
        MongoTestDto source = new MongoTestDto() ;
        source.setId(207);
        source.setUserName("kweitan");
        source.setAge("40");
        mongoTestService.saveMongoTest(source);
    }

    @Test
    public void updateMongoTest() {
        MongoTestDto source = new MongoTestDto() ;
        source.setId(200);
        source.setUserName("guixin");
        source.setAge("20");
        mongoTestService.updateMongoTest(source);
    }

    @Test
    public void deleteMongoTest() {
        System.out.println(mongoTestService.deleteMongoTest(200));
    }

    @Test
    public void findMongoTestByName() {
        List<MongoTestDto> list = mongoTestService.findMongoTestByName("kweitan");
        System.out.println(list.size());
    }

    @Test
    public void findMongoTestByAge() {
        List<MongoTestDto> list = mongoTestService.findMongoTestByAge("30");
        System.out.println(list.size());
    }

    @Test
    public void findMongoTestById() {
        mongoTestService.findMongoTestById(200);
    }

    @Test
    public void findAllByPageable(){
        Page<MongoTestDto> list = mongoTestService.findAllByPageable(0,10);
        System.out.println(list.getTotalElements());
    }
}