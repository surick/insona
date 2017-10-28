package com.jieweifu.controllers;

import com.jieweifu.models.ResultModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@SuppressWarnings("unused")
@Controller
public class ErrorController implements org.springframework.boot.autoconfigure.web.ErrorController {
    private final static String ERROR_PATH = "/error";
    private ErrorAttributes errorAttributes;
    private final static Logger logger = LoggerFactory.getLogger(ErrorController.class);

    @Autowired
    public ErrorController(ErrorAttributes errorAttributes) {
        this.errorAttributes = errorAttributes;
    }

    @RequestMapping(ERROR_PATH)
    @ResponseBody
    public ResultModel error(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Object> map = this.errorAttributes.getErrorAttributes(new ServletRequestAttributes(request), false);
        Integer status = (Integer) map.get("status");
        response.setStatus(HttpStatus.OK.value());

        logger.error(String.valueOf(map.get("message")), this.errorAttributes.getError(new ServletRequestAttributes(request)));
        String message = "出错了, 请稍后再试!";
        if (logger.isDebugEnabled()) {
            message = String.valueOf(map.get("message"));
        }

        return new ResultModel().setError(status, message);
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}
