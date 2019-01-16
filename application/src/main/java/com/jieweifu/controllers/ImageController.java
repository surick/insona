package com.jieweifu.controllers;

import com.baidu.ueditor.ActionEnter;
import com.froala.editor.Image;
import com.froala.editor.image.ImageOptions;
import com.jieweifu.models.insona.Home;
import com.jieweifu.models.insona.Material;
import com.jieweifu.services.insona.HomeService;
import com.jieweifu.services.insona.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Controller("Image")
@RequestMapping("image")
public class ImageController {

    private MaterialService materialService;
    private HomeService homeService;

    @Autowired
    public ImageController(HomeService homeService,
                           MaterialService materialService) {
        this.homeService = homeService;
        this.materialService = materialService;
    }


    @Value("${custom.upload.material}")
    private String materialUpload;
    @Value("${custom.upload.dir}")
    private String uploadPath;
    @Value("${custom.upload.home}")
    private String homeUpload;

    @RequestMapping("config")
    public void config(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json");
        String rootPath = request.getSession().getServletContext().getRealPath("/");
        try {
            String exec = new ActionEnter(request, rootPath).exec();
            PrintWriter writer = response.getWriter();
            writer.write(exec);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

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
            Image.upload(request, path, options).forEach((key, value) -> responseData.put(key, "http://insona.d-health.cn/imgs/uploads/images/" + value));
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

    @PostMapping("homeUpload")
    @ResponseBody
    public Map<Object, Object> homeUpload(HttpServletRequest request,
                                          @RequestParam("id") String id,
                                          @RequestParam("title") String title,
                                          @RequestParam(value = "height", defaultValue = "300") Integer height,
                                          @RequestParam(value = "width", defaultValue = "300") Integer width,
                                          @RequestParam(value = "keepAspectRatio", defaultValue = "false") boolean keepAspectRatio,
                                          @RequestParam(value = "onlyThumb", defaultValue = "false") boolean onlyThumb,
                                          @RequestParam(value = "noThumb", defaultValue = "false") boolean noThumb) {
        ImageOptions options = new ImageOptions();
        options.setResize(width, height, keepAspectRatio);
        if (onlyThumb)
            options.setOnlyThumb(true);
        if (noThumb) {
            options = null;
        }
        Map<Object, Object> responseData = homeUploadImage(request, homeUpload, options);
        Home home = new Home();
        home.setId(Integer.parseInt(id));
        home.setTitle(title);
        home.setImgUrl((String) responseData.get("link"));
        homeService.updateHome(home);
        return responseData;
    }

    private Map<Object, Object> homeUploadImage(HttpServletRequest request, String path, ImageOptions options) {
        Map<Object, Object> responseData = new HashMap<>();
        try {
            Image.upload(request, path, options).forEach((key, value) -> responseData.put(key, "/uploads/home/" + value));
        } catch (Exception e) {
            e.printStackTrace();
            responseData.put("error", e.toString());
        }
        return responseData;
    }

    @PostMapping("materialUpload")
    @ResponseBody
    public Map<Object, Object> materialUpload(HttpServletRequest request,
                                              @RequestParam("id") String id,
                                              @RequestParam("title") String title,
                                              @RequestParam(value = "height", defaultValue = "300") Integer height,
                                              @RequestParam(value = "width", defaultValue = "300") Integer width,
                                              @RequestParam(value = "keepAspectRatio", defaultValue = "false") boolean keepAspectRatio,
                                              @RequestParam(value = "onlyThumb", defaultValue = "false") boolean onlyThumb,
                                              @RequestParam(value = "noThumb", defaultValue = "false") boolean noThumb) {
        ImageOptions options = new ImageOptions();
        options.setResize(width, height, keepAspectRatio);
        if (onlyThumb)
            options.setOnlyThumb(true);
        if (noThumb) {
            options = null;
        }
        Map<Object, Object> responseData = materialUploadImage(request, materialUpload, options);
        Material material = new Material();
        material.setId(Integer.parseInt(id));
        material.setTitle(title);
        material.setImgUrl((String) responseData.get("link"));
        materialService.updateMaterial(material);
        return materialUploadImage(request, materialUpload, options);
    }

    private Map<Object, Object> materialUploadImage(HttpServletRequest request, String path, ImageOptions options) {
        Map<Object, Object> responseData = new HashMap<>();
        try {
            Image.upload(request, path, options).forEach((key, value) -> responseData.put(key, "/uploads/material/" + value));
        } catch (Exception e) {
            e.printStackTrace();
            responseData.put("error", e.toString());
        }
        return responseData;
    }
}
