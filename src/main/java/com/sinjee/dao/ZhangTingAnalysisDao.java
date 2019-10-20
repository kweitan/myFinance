package com.sinjee.dao;

import com.sinjee.entity.ZhangTingAnalysisEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author 小小极客
 * 时间 2019/10/13 19:34
 * @ClassName ZhangTingAnalysisDao
 * 描述 涨停分析DAO类
 **/
@Repository
public class ZhangTingAnalysisDao {

    @Autowired
    private MongoTemplate mongoTemplate ;

    /**
     * 保存对象
     * @param zhangTingAnalysisEntity
     * @return
     */
    public ZhangTingAnalysisEntity save(ZhangTingAnalysisEntity zhangTingAnalysisEntity){
        return mongoTemplate.save(zhangTingAnalysisEntity) ;
    }


    /**
     * 根据交易日期 连扳数 获取数据
     * @param tradeDate
     * @param zhangtingNum
     * @param page
     * @param size
     * @return
     */
    public Page<ZhangTingAnalysisEntity> getPageByTradeDate(int tradeDate,int zhangtingNum,int page, int size){

        Pageable pageable = PageRequest.of(page,size);

        //创建查询对象
//        Criteria criteria = Criteria
//                Criteria.where("trade_date").is(tradeDate).andOperator(Criteria.where("zhangtingNum").gte(zhangtingNum)) ;

        Criteria criteria = new Criteria();
        criteria.andOperator(Criteria.where("trade_date").is(tradeDate),Criteria.where("zhangtingNum").gte(zhangtingNum)) ;
        Query query = new Query(criteria);

//        query.with(pageable) ;

        //查询当前页数据集合
        List<ZhangTingAnalysisEntity> list = mongoTemplate.find(query,ZhangTingAnalysisEntity.class) ;

        //查询总记录数
        long count=(int) mongoTemplate.count(query,ZhangTingAnalysisEntity.class);

        //创建分页实体对象
        return new PageImpl<ZhangTingAnalysisEntity>(list,pageable,count);
    }

}
