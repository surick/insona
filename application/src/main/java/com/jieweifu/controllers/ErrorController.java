package com.jieweifu.controllers;

import com.jieweifu.common.utils.ClientUtil;
import com.jieweifu.models.Result;
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
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
    public Result error(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<String, Object> map = errorAttributes.getErrorAttributes(new ServletRequestAttributes(request), false);
        Integer status = (Integer) map.get("status");
        response.setStatus(HttpStatus.OK.value());

        Throwable error = errorAttributes.getError(new ServletRequestAttributes(request));
        String message = "出错了, 请稍后再试!";
        if (error instanceof ConstraintViolationException) {
            if (logger.isDebugEnabled()) {
                List<String> errors = new ArrayList<>();
                ((ConstraintViolationException) error).getConstraintViolations().forEach(p -> errors.add(p.getMessage()));
                message = String.join(", ", errors);
            }
        } else {
            logger.error("ip: "
                    .concat(ClientUtil.getClientIp(request))
                    .concat(", status: ")
                    .concat(String.valueOf(map.get("status")))
                    .concat(", path: ")
                    .concat(String.valueOf(map.get("path")))
                    .concat(", message: ")
                    .concat(String.valueOf(map.get("message"))), error);
            if (logger.isDebugEnabled()) {
                message = error != null ? error.getLocalizedMessage() : String.valueOf(map.get("message"));
            }
        }

        return new Result().setError(status, message);
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}
