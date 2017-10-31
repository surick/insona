package com.jieweifu.common.utils;

import org.springframework.validation.Errors;

import java.util.ArrayList;
import java.util.List;

public class ErrorUtil {
    public static String getErrors(Errors errors){
        List<String> errorMessages = new ArrayList<>();
        errors.getFieldErrors().forEach(p-> errorMessages.add(p.getDefaultMessage()));
        return  String.join(", ", errorMessages);
    }
}
