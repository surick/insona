package com.jieweifu.configurations;

import com.jieweifu.common.utils.TokenUtil;
import com.jieweifu.interceptors.AdminAuthInterceptor;
import com.jieweifu.interceptors.LimitInterceptor;
import com.jieweifu.interceptors.OptionsInterceptor;
import com.jieweifu.services.admin.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    private TokenUtil tokenUtil;
    private LogService logService;

    @Autowired
    public WebMvcConfig(TokenUtil tokenUtil, LogService logService) {
        this.tokenUtil = tokenUtil;
        this.logService = logService;
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseSuffixPatternMatch(true).setUseTrailingSlashMatch(true);
        super.configurePathMatch(configurer);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new OptionsInterceptor());
        registry.addInterceptor(new LimitInterceptor(tokenUtil.getRedisUtil()));
        registry.addInterceptor(new AdminAuthInterceptor(tokenUtil, logService)).addPathPatterns("/sys/**", "/insona/**");
    }
}
