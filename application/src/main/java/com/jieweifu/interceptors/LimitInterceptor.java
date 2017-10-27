package com.jieweifu.interceptors;

import com.jieweifu.common.utils.ClientUtil;
import com.jieweifu.common.utils.RedisUtil;
import com.jieweifu.constants.CommonConstant;
import com.jieweifu.models.ResultModel;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

public class LimitInterceptor extends HandlerInterceptorAdapter {

    private RedisUtil redisUtil;
    private final static Logger logger = LoggerFactory.getLogger(LimitInterceptor.class);

    public LimitInterceptor(RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equalsIgnoreCase(HttpMethod.OPTIONS.name())) {
            return true;
        }

        String ip = ClientUtil.getClientIp(request);
        String url = request.getRequestURL().toString();
        String key = DigestUtils.md5Hex(String.format("%s%s%s", CommonConstant.REQUEST_LIMIT, url, ip));

        int count = 0;
        if (redisUtil.hasKey(key)) {
            count = Integer.parseInt(redisUtil.get(key).toString());
            ++count;
            redisUtil.incr(key, 1);
        } else {
            redisUtil.setEX(key, ++count, CommonConstant.LIMIT_TIME, TimeUnit.SECONDS);
        }

        if (count > CommonConstant.LIMIT_COUNT) {
            writeLimitResponse(response);
            logger.error(HttpStatus.TOO_MANY_REQUESTS.name(), String.format("ip: %s, url: %s", ip, url));
            return false;
        }

        return super.preHandle(request, response, handler);
    }

    private void writeLimitResponse(HttpServletResponse response) throws Exception {
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().print(new ResultModel().setError(HttpStatus.TOO_MANY_REQUESTS.value(), "访问过于频繁").toJSON());
        response.flushBuffer();
    }
}
