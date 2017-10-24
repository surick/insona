package com.jieweifu.common.dbservice;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Documented
public @interface Column {
    boolean insert() default false;

    boolean update() default false;

    String column() default "";

    boolean primaryKey() default false;
}
