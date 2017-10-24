package com.jieweifu.controllers.admin;

import com.jieweifu.interceptors.AdminAuthAnnotation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@SuppressWarnings("unused")
@Controller("AdminMenu")
@RequestMapping("sys/menu")
@AdminAuthAnnotation
public class MenuController {

}
