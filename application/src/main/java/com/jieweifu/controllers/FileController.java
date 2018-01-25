package com.jieweifu.controllers;

import com.froala.editor.File;
import com.froala.editor.file.FileOptions;
import com.froala.editor.file.FileValidation;
import com.jieweifu.common.business.BaseContextHandler;
import com.jieweifu.common.utils.RedisUtil;
import com.jieweifu.models.admin.User;
import com.jieweifu.models.insona.Document;
import com.jieweifu.services.insona.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller("File")
@RequestMapping("file")
public class FileController {

    private DocumentService documentService;

    private RedisUtil redisUtil;

    @Autowired
    public FileController(DocumentService documentService,RedisUtil redisUtil) {
        this.documentService = documentService;
        this.redisUtil = redisUtil;
    }

    @Value("${custom.upload.dir}")
    private String uploadPath;

    @Value("${custom.upload.home}")
    private String DocumentUpload;

    @PostMapping("upload")
    @ResponseBody
    public Map<Object, Object> upload(HttpServletRequest request) {
        FileOptions options = new FileOptions();
        Map<Object, Object> responseData = new HashMap<>();
        options.setValidation(new FileValidation());
        try {
            File.upload(request, uploadPath, options).forEach((key, value) -> responseData.put(key, "/uploads/images/" + value));
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
    @PostMapping("DocumentUpload")
    @ResponseBody
    public Map<Object, Object> DocumentUpload(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        FileOptions options = new FileOptions();
        Map<Object, Object> responseData = new HashMap<>();
        options.setValidation(new FileValidation());
        try {
            File.upload(request, DocumentUpload, options).forEach((key, value) -> responseData.put(key, "/uploads/home/" + value));
        } catch (Exception e) {
            e.printStackTrace();
            responseData.put("error", e.toString());
        }
        Document document = new Document();
        document.setName(file.getOriginalFilename());
        document.setFileType("全部");
        document.setFileUrl(String.valueOf(responseData.get("link")));
        int total = documentService.getDocumentTotal();
        document.setSortNo(total + 1);
        document.setIsDelete(0);
        String label = (String) redisUtil.get("label");
        document.setLabel(label);
        documentService.saveDocument(document);
        return responseData;
    }
}
