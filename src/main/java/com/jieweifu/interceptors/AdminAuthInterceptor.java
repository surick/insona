package com.jieweifu.interceptors;

import com.jieweifu.common.business.BaseContextHandler;
import com.jieweifu.constants.CommonConstant;
import com.jieweifu.models.ResultModel;
import com.jieweifu.common.utils.TokenUtil;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AdminAuthInterceptor extends HandlerInterceptorAdapter {

    private TokenUtil tokenUtil;

    public AdminAuthInterceptor(TokenUtil tokenUtil) {
        this.tokenUtil = tokenUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(request.getMethod().equalsIgnoreCase(HttpMethod.OPTIONS.name())){
            return true;
        }

        HandlerMethod methodHandler = (HandlerMethod) handler;
        AdminAuthAnnotation classAuth = methodHandler.getBeanType().getAnnotation(AdminAuthAnnotation.class);
        AdminAuthAnnotation methodAuth = methodHandler.getMethod().getAnnotation(AdminAuthAnnotation.class);
        boolean needAuth = true;
        if (classAuth != null && !classAuth.check()) {
            needAuth = false;
        }
        if (methodAuth != null && !methodAuth.check()) {
            needAuth = false;
        }
        if (!needAuth) {
            return true;
        }

        String authToken = request.getHeader(CommonConstant.TOKEN_AUTHORIZATION);
        String userId = tokenUtil.getUserInfoToken(authToken);
        if (userId == null) {
            writeUnAuthorization(response);
            return false;
        }

        //此处从缓存中获取用户权限
        String path = request.getServletPath();
        String method = request.getMethod();



        writeUnAuthorization(response);

        request.setAttribute(CommonConstant.USER, "");
        return true;
    }

    private void writeUnAuthorization(HttpServletResponse response) throws Exception {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().print(new ResultModel().setError(HttpStatus.UNAUTHORIZED.value(), "未授权或授权已过期").toJSON());
        response.flushBuffer();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        BaseContextHandler.remove();
        super.afterCompletion(request, response, handler, ex);
    }
}
