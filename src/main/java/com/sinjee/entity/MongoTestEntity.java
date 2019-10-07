package com.sinjee.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * @ClassName MongoTestEntity
 * @Description 实体测试类
 * @Author pc
 * @Date 2019/10/5 22:28
 * @Version 1.0
 **/
@Data
@Document(collection = "mongoTestEntity")
public class MongoTestEntity implements Serializable {

    @Id
    private Integer id ;

    private String userName ;

    private String age ;

}
