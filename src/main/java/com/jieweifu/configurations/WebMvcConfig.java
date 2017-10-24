package com.jieweifu.configurations;

import com.jieweifu.interceptors.AdminAuthInterceptor;
import com.jieweifu.common.utils.TokenUtil;
import com.jieweifu.interceptors.LimitInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.stream.Stream;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

    private Environment environment;
    private TokenUtil tokenUtil;

    @Autowired
    public WebMvcConfig(Environment environment, TokenUtil tokenUtil) {
        this.environment = environment;
        this.tokenUtil = tokenUtil;
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        configurer.setUseSuffixPatternMatch(true).setUseTrailingSlashMatch(true);
        super.configurePathMatch(configurer);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LimitInterceptor(tokenUtil.getRedisUtil()));
        registry.addInterceptor(new AdminAuthInterceptor(tokenUtil)).addPathPatterns("/sys/**");
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        if (Stream.of(environment.getActiveProfiles()).filter(profile -> profile.equalsIgnoreCase("dev")).count() > 0) {
            registry.addMapping("/**")
                    .allowedOrigins("*")
                    .allowedMethods("*")
                    .allowedHeaders("*")
                    .allowCredentials(false);
        }
        super.addCorsMappings(registry);
    }
}
