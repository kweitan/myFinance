package com.sinjee.dao;

import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.sinjee.entity.MongoTestEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName MongoTestDao
 * @Description TODO
 * @Author pc
 * @Date 2019/10/5 22:32
 * @Version 1.0
 **/
@Repository
public class MongoTestDao {

    @Autowired
    private MongoTemplate mongoTemplate ;

    /**
     * 创建对象
     * @param mongoTestEntity
     */
    public MongoTestEntity saveMongoTest(MongoTestEntity mongoTestEntity){
        return mongoTemplate.save(mongoTestEntity) ;
    }

    /**
     * 根据用户名查询对象
     * @param userName
     * @return
     */
    public List<MongoTestEntity> findMongoTestByName(String userName){
        Query query = new Query(Criteria.where("userName").is(userName)) ;
        List<MongoTestEntity> lists = mongoTemplate.find(query,MongoTestEntity.class) ;
        return lists ;
    }

    /**
     * 更新对象
     * @param mongoTestEntity
     * @return
     */
    public long updateMongoTestEntity(MongoTestEntity mongoTestEntity){
        Query query = new Query(Criteria.where("id").is(mongoTestEntity.getId())) ;
        Update update = new Update().set("age",mongoTestEntity.getAge()).set("userName",mongoTestEntity.getUserName());
        UpdateResult updateResult = mongoTemplate.updateFirst(query,update,MongoTestEntity.class);
        return updateResult.getModifiedCount() ;
    }

    /**
     * 删除对象
     * @param id
     * @return
     */
    public long deleteMongoTestEntity(Integer id){
        Query query = new Query(Criteria.where("id").is(id)) ;
        DeleteResult deleteResult = mongoTemplate.remove(query,MongoTestEntity.class);
        return deleteResult.getDeletedCount() ;
    }

    /**
     * 根据年龄查找列表
     * @param age
     * @return
     */
    public List<MongoTestEntity> findMongoTestByAge(String age){
        Query query = new Query(Criteria.where("age").is(age)) ;
        List<MongoTestEntity> lists = mongoTemplate.find(query,MongoTestEntity.class) ;
        return lists ;
    }

    /**
     * 根据ID返回对象
     * @param id
     * @return
     */
    public MongoTestEntity findMongoTestById(Integer id){
        Query query = new Query(Criteria.where("id").is(id)) ;
        MongoTestEntity mongoTestEntity = mongoTemplate.findOne(query,MongoTestEntity.class);
        return  mongoTestEntity;
    }

    public Page<MongoTestEntity> findAllByPageable(int page, int size){
        Pageable pageable = PageRequest.of(page,size);

        //创建查询对象
        Query query = new Query();

        //设置分页
        query.with(pageable);

        //查询当前页数据集合
        List<MongoTestEntity>  list = mongoTemplate.find(query,MongoTestEntity.class);

        //查询总记录数
        long count=(int) mongoTemplate.count(query,MongoTestEntity.class);

        //创建分页实体对象

        return new PageImpl<MongoTestEntity>(list,pageable,count);
    }

}
