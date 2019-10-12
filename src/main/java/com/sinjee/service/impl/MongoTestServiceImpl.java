package com.sinjee.service.impl;

import com.sinjee.dao.MongoTestDao;
import com.sinjee.dto.MongoTestDto;
import com.sinjee.entity.MongoTestEntity;
import com.sinjee.service.MongoTestService;
import com.sinjee.tools.BeanConversionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @ClassName MongoTestService
 * @Description MongoTestServiceImpl
 * @Author pc
 * @Date 2019/10/6 1:14
 * @Version 1.0
 **/
@Service
public class MongoTestServiceImpl implements MongoTestService {

    @Autowired
    private MongoTestDao mongoTestDao ;

    @Override
    @Transactional
    public MongoTestDto saveMongoTest(MongoTestDto mongoTestDto) {
        MongoTestEntity mongoTestEntity = new MongoTestEntity() ;
        BeanUtils.copyProperties(mongoTestDto,mongoTestEntity);
        MongoTestEntity resultEntity = mongoTestDao.saveMongoTest(mongoTestEntity);
        MongoTestDto resultDto = new MongoTestDto() ;
        BeanUtils.copyProperties(resultEntity,resultDto);
        return resultDto;
    }

    @Override
    @Transactional
    public long updateMongoTest(MongoTestDto mongoTestDto) {
        MongoTestEntity mongoTestEntity = new MongoTestEntity() ;
        BeanUtils.copyProperties(mongoTestDto,mongoTestEntity);
        return mongoTestDao.updateMongoTestEntity(mongoTestEntity);
    }

    @Override
    public long deleteMongoTest(Integer id) {
        return mongoTestDao.deleteMongoTestEntity(id);
    }

    @Override
    public List<MongoTestDto> findMongoTestByName(String name) {
        List<MongoTestDto> dtoList = BeanConversionUtils.listCopyToAnotherList(MongoTestDto.class,
                mongoTestDao.findMongoTestByName(name)) ;
        return dtoList;
    }

    @Override
    public List<MongoTestDto> findMongoTestByAge(String age) {
        List<MongoTestDto> dtoList = BeanConversionUtils.listCopyToAnotherList(MongoTestDto.class,
                mongoTestDao.findMongoTestByAge(age)) ;
        return dtoList;
    }

    @Override
    public MongoTestDto findMongoTestById(Integer id) {
        MongoTestEntity mongoTestEntity = mongoTestDao.findMongoTestById(id) ;
        MongoTestDto resultDto = null ;
        if (null != mongoTestEntity){
            resultDto = new MongoTestDto() ;
            BeanUtils.copyProperties(mongoTestEntity,resultDto);
        }
        return resultDto;
    }

    @Override
    public Page<MongoTestDto> findAllByPageable(int page, int size) {
        Page<MongoTestEntity> pageModel = mongoTestDao.findAllByPageable(page,size);
        List<MongoTestDto> dtoList = null ;
        if (null != pageModel){
            dtoList = BeanConversionUtils.listCopyToAnotherList(MongoTestDto.class,pageModel.getContent()) ;
        }
        return new PageImpl(dtoList,pageModel.getPageable(),pageModel.getTotalElements());
    }
}
