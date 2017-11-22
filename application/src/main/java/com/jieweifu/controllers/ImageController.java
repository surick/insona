package com.jieweifu.controllers;

import com.froala.editor.Image;
import com.froala.editor.image.ImageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller("Image")
@RequestMapping("image")
public class ImageController {

    @Value("${custom.upload.dir}")
    private String uploadPath;

    @PostMapping("upload")
    @ResponseBody
    public Map<Object, Object> upload(HttpServletRequest request,
                                      @RequestParam(value = "height", defaultValue = "300") Integer height,
                                      @RequestParam(value = "width", defaultValue = "300") Integer width,
                                      @RequestParam(value = "keepAspectRatio", defaultValue = "false") boolean keepAspectRatio,
                                      @RequestParam(value = "onlyThumb", defaultValue = "false") boolean onlyThumb,
                                      @RequestParam(value = "noThumb", defaultValue = "false") boolean noThumb) {
        ImageOptions options = new ImageOptions();
        options.setResize(width, height, keepAspectRatio);
        if (onlyThumb) {
            options.setOnlyThumb(true);
        }
        if (noThumb) {
            options = null;
        }
        return uploadImage(request, uploadPath, options);
    }

    private Map<Object, Object> uploadImage(HttpServletRequest request, String path, ImageOptions options) {
        Map<Object, Object> responseData = new HashMap<>();
        try {
            Image.upload(request, path, options).forEach((key, value) -> responseData.put(key, "http://localhost:8080/uploads/images/" + value));
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
            Image.delete(request, src);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";
    }
}
