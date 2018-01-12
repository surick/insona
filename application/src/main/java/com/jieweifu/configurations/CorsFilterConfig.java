package com.jieweifu.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@SuppressWarnings("all")
@Component
public class CorsFilterConfig implements Filter {

    private Environment environment;

    @Autowired
    public CorsFilterConfig(Environment environment){
        this.environment = environment;
    }

    @Override
    public void init(javax.servlet.FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain   chain) throws IOException, ServletException {
        if (Arrays.stream(environment.getActiveProfiles()).filter(profile -> profile.equalsIgnoreCase("dev")).count() > 0) {
            HttpServletResponse response = (HttpServletResponse) res;
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
            response.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, TokenAuthorization");
        }
        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {}

}