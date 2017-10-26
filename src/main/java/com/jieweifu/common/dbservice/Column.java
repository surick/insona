package com.jieweifu.common.dbservice;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Documented
public @interface Column {
    boolean insert() default true;

    boolean update() default true;

    boolean select() default true;

    String columnName() default "";

    boolean primaryKey() default false;
}
