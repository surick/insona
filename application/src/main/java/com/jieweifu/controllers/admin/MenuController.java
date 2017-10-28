package com.jieweifu.controllers.admin;

import com.jieweifu.interceptors.AdminAuthAnnotation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理菜单类
 */
@SuppressWarnings("unused")
@RestController("SystemMenu")
@RequestMapping("sys/menu")
@AdminAuthAnnotation
public class MenuController {

}
