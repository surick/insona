package com.jieweifu.interceptors;

import java.lang.annotation.*;

@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface AdminAuthAnnotation {
    boolean check() default true;
}
