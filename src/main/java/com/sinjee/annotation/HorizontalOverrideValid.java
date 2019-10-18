package com.sinjee.annotation;

import org.springframework.data.mongodb.core.mapping.Document;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解 横向越权校验
 */
@Target({ElementType.PARAMETER,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Document
public @interface HorizontalOverrideValid {
    //调用方法名
    String callMethodName() default "" ;

    //越权访问后 重定向地址
    String returnUrl() default "" ;
}
