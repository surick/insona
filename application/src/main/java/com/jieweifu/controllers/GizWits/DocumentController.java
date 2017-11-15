package com.jieweifu.controllers.GizWits;

import com.jieweifu.common.utils.ErrorUtil;
import com.jieweifu.models.Result;
import com.jieweifu.models.gizWits.Document;
import com.jieweifu.services.gizWits.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@SuppressWarnings("unused")
@RestController("GizWitsDocument")
@RequestMapping("giz/document")
public class DocumentController {
    private DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    /**
     * 新增document
     *
     * @param document document
     * @param errors   判断字段
     * @return message
     */
    @PostMapping("saveDocument")
    public Result saveDocument(@Valid @RequestBody Document document, Errors errors) {
        if (errors.hasErrors())
            return new Result().setError(ErrorUtil.getErrors(errors));
        if (documentService.AllDocumentByName(document.getName()) != null)
            return new Result().setError("已存在");
        documentService.saveDocument(document);
        return new Result().setMessage("新增成功");
    }

    /**
     * 删除document
     *
     * @param id id
     * @return message
     */
    @DeleteMapping("removeDocument")
    public Result removeDocument(@Param("id") Integer id) {
        if (id == 0)
            return new Result().setError("id不能为空");
        if (documentService.getDocument(id) == null)
            return new Result().setError("不存在");
        documentService.removeDocument(id);
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
     * @param errors   判断字段
     * @return message
     */
    @PutMapping("updateDocument")
    public Result updateDocument(@Valid @RequestBody Document document, Errors errors) {
        if (errors.hasErrors())
            return new Result().setError(ErrorUtil.getErrors(errors));
        if (documentService.getDocument(document.getId()) == null)
            return new Result().setError("不存在");
        documentService.updateDocument(document);
        return new Result().setMessage("修改成功");
    }
}
