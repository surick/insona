package com.jieweifu.controllers.other;

import com.froala.editor.file.FileOptions;
import com.froala.editor.file.FileValidation;
import com.jieweifu.models.Result;
import com.jieweifu.models.gizWits.Document;
import com.jieweifu.services.gizWits.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
@RestController("GizWitsDocument")
@RequestMapping("giz/document")
public class DocumentController {
    private DocumentService documentService;

    @Value("${custom.upload.home}")
    private String uploadPath;

    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    /**
     * 新增document
     *
     * @param document document
     * @return message
     */
    @PostMapping("saveDocument")
    public Result saveDocument(@Valid @RequestBody Document document) {
        documentService.saveDocument(document);
        return new Result().setMessage("新增成功");
    }

    /**
     * 删除document
     *
     * @param ids id数组
     * @return message
     */
    @DeleteMapping("removeDocument")
    public Result removeDocument(@RequestBody List<String> ids) {
        for (String id : ids) {
            if (documentService.getDocument(Integer.parseInt(id)) == null) {
                return new Result().setError("无效id");
            }
            documentService.removeDocument(Integer.parseInt(id));
        }
        return new Result().setMessage("删除成功");
    }

    /**
     * 按name查找document
     *
     * @param name name
     * @return document
     */
    @GetMapping("AllDocumentByName")
    public Result AllDocumentByName(@Param("name") String name) {
        if (name.length() < 1)
            return new Result().setError("name不能为空");
        Document document = documentService.AllDocumentByName(name);
        return new Result().setData(document);
    }

    /**
     * 按type查找document
     *
     * @param type type
     * @return document
     */
    @GetMapping("AllDocumentByType")
    public Result AllDocumentByType(@Param("type") String type) {
        if (type.length() < 1)
            return new Result().setError("type不能为空");
        List<Document> documentList = documentService.AllDocumentByType(type);
        return new Result().setData(documentList);
    }

    /**
     * 修改Document
     *
     * @param document Document
     * @return message
     */
    @PutMapping("updateDocument")
    public Result updateDocument(@Valid @RequestBody Document document) {
        if (documentService.getDocument(document.getId()) == null)
            return new Result().setError("不存在");
        documentService.updateDocument(document);
        return new Result().setMessage("修改成功");
    }

    /**
     * 分页查询
     *
     * @param pageIndex 起始页
     * @param pageSize  条目数
     * @return map
     */
    @GetMapping("pageDocument/{pageIndex}/{pageSize}")
    public Result getInfoByPage(@PathVariable("pageIndex") int pageIndex,
                                @PathVariable("pageSize") int pageSize) {
        if (pageIndex < 0 || pageSize < 0)
            return new Result().setError("页码或条目数不合法");
        List<Document> infoList = documentService.documentPage(pageIndex, pageSize);
        int total = documentService.getDocumentTotal();
        Map<String, Object> map = new HashMap<>();
        map.put("list", infoList);
        map.put("total", total);
        return new Result().setData(map);
    }

    @PostMapping("upload")
    @ResponseBody
    public Map<Object, Object> upload(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        FileOptions options = new FileOptions();
        Map<Object, Object> responseData = new HashMap<>();
        options.setValidation(new FileValidation());
        try {
            com.froala.editor.File.upload(request, uploadPath, options).forEach((key, value) -> responseData.put(key, "/uploads/home/" + value));
        } catch (Exception e) {
            e.printStackTrace();
            responseData.put("error", e.toString());
        }
        Document document = new Document();
        document.setName(file.getOriginalFilename());
        document.setFileType(1);
        document.setFileUrl(String.valueOf(responseData.get("link")));
        int total = documentService.getDocumentTotal();
        document.setSortNo(total + 1);
        document.setIsDelete(0);
        saveDocument(document);
        return responseData;
    }
}
