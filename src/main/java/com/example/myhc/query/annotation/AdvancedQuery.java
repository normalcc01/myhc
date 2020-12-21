package com.example.myhc.query.annotation;

import com.example.myhc.query.enums.QueryMethodEnum;

import java.lang.annotation.*;

/**
 * 查询组件
 *
 *
 *
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface AdvancedQuery {

    QueryMethodEnum method();

    String tableField() default "";

}
