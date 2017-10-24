package com.jieweifu.controllers;

import com.froala.editor.File;
import com.froala.editor.file.FileOptions;
import com.froala.editor.file.FileValidation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller("File")
@RequestMapping("file")
public class FileController {

    @Value("${custom.upload.dir}")
    private String uploadPath;

    @PostMapping("upload")
    @ResponseBody
    public Map<Object, Object> upload(HttpServletRequest request) {
        FileOptions options = new FileOptions();
        Map<Object, Object> responseData = new HashMap<>();
        options.setValidation(new FileValidation());
        try {
            File.upload(request, uploadPath, options).forEach((key, value) -> responseData.put(key, "/uploads/" + value));
        } catch (Exception e) {
            e.printStackTrace();
            responseData.put("error", e.toString());
        }
        return responseData;
    }

    @PostMapping("delete")
    @ResponseBody
    public String delete(HttpServletRequest request, @RequestParam String src) {
        try {
            File.delete(request, src);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }
}
