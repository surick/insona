package com.jieweifu.common.dbservice;

import java.lang.annotation.*;

/**
 * Created by Administrator on 2017-06-16.
 */

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Entity {
    String tableName() default "";
}
