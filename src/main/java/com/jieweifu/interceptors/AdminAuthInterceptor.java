package com.jieweifu.interceptors;

import com.jieweifu.common.business.BaseContextHandler;
import com.jieweifu.common.utils.ClientUtil;
import com.jieweifu.constants.CommonConstant;
import com.jieweifu.models.ResultModel;
import com.jieweifu.common.utils.TokenUtil;
import com.jieweifu.models.admin.LogModel;
import com.jieweifu.services.admin.LogService;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;

public class AdminAuthInterceptor extends HandlerInterceptorAdapter {

    private TokenUtil tokenUtil;
    private LogService logService;

    public AdminAuthInterceptor(TokenUtil tokenUtil, LogService logService) {
        this.tokenUtil = tokenUtil;
        this.logService = logService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equalsIgnoreCase(HttpMethod.OPTIONS.name())) {
            return true;
        }
        BaseContextHandler.setRequestIp(ClientUtil.getClientIp(request));
        BaseContextHandler.setActionStartTime();

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
        String userId = tokenUtil.getUserId(authToken);
        if (userId == null) {
            writeUnAuthorization(request, response);
            return false;
        }

        tokenUtil.cacheUserInThreadLocal(Integer.parseInt(userId));

        String path = request.getServletPath();
        String method = request.getMethod();
        boolean canAction = tokenUtil.checkAuthorization(path, method);
        if (!canAction) {
            writeUnAuthorization(request, response);
            return false;
        }
        return true;
    }

    private void writeUnAuthorization(HttpServletRequest request, HttpServletResponse response) throws Exception {
        afterCompletion(request, response, false, new RuntimeException("未授权或授权已过期"));

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().print(new ResultModel().setError(HttpStatus.UNAUTHORIZED.value(), "未授权或授权已过期").toJSON());
        response.flushBuffer();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        BaseContextHandler.setActionEndTime();

        logService.log(request.getServletPath(), request.getMethod(), !handler.equals(false));

        BaseContextHandler.remove();
        super.afterCompletion(request, response, handler, ex);
    }
}
