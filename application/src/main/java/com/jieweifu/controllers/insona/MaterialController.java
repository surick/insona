package com.jieweifu.controllers.insona;

import com.froala.editor.Image;
import com.froala.editor.image.ImageOptions;
import com.jieweifu.common.utils.ErrorUtil;
import com.jieweifu.models.Result;
import com.jieweifu.models.insona.Material;
import com.jieweifu.services.insona.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
@RestController("InsonaMaterial")
@RequestMapping("insona/material")
public class MaterialController {

    private MaterialService materialService;

    @Value("${custom.upload.material}")
    private String uploadPath;

    @Autowired
    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    /**
     * 新增
     *
     * @param material material
     * @param errors   判断字段
     * @return message
     */
    @PostMapping("saveMaterial")
    public Result saveMaterial(@Valid @RequestBody Material material, Errors errors) {
        if (errors.hasErrors())
            return new Result().setError(ErrorUtil.getErrors(errors));
        material.setIsDeleted(0);
        materialService.saveMaterial(material);
        return new Result().setMessage("新增成功");
    }

    /**
     * 删除
     *
     * @param ids id数组
     * @return message
     */
    @DeleteMapping("removeMaterial")
    public Result removeMaterial(@RequestBody List<String> ids) {
        for (String id : ids) {
            if (materialService.getMaterial(Integer.parseInt(id)) == null) {
                return new Result().setError("无效id");
            }
            materialService.removeMaterial(Integer.parseInt(id));
        }
        return new Result().setMessage("删除成功");
    }

    /**
     * 修改
     *
     * @param material material
     * @return message
     */
    @PutMapping("updateMaterial")
    public Result updateMaterial(@RequestBody Material material) {
        if (materialService.getMaterial(material.getId()) == null)
            return new Result().setError("不存在");
        materialService.updateMaterial(material);
        return new Result().setMessage("修改成功");
    }

    /**
     * 分页查询
     *
     * @param pageIndex 页码
     * @param pageSize  页条目数
     * @return map
     */
    @GetMapping("listMaterial/{pageIndex}/{pageSize}")
    public Result listMaterial(@PathVariable("pageIndex") Integer pageIndex,
                               @PathVariable("pageSize") Integer pageSize) {
        if (pageIndex < 0 || pageSize < 0)
            return new Result().setError("页码或条目数不合法");
        List<Material> materialList = materialService.listMaterial(pageIndex, pageSize);
        int total = materialService.getMaterialTotal();
        Map<String, Object> map = new HashMap<>();
        map.put("list", materialList);
        map.put("total", total);
        return new Result().setData(map);
    }

    @PostMapping("upload")
    @ResponseBody
    public Map<Object, Object> upload(HttpServletRequest request,
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
        Map<Object, Object> responseData = uploadImage(request, uploadPath, options);
        Material material = new Material();
        material.setId(Integer.parseInt(id));
        material.setTitle(title);
        material.setImgUrl((String) responseData.get("link"));
        updateMaterial(material);
        return uploadImage(request, uploadPath, options);
    }

    private Map<Object, Object> uploadImage(HttpServletRequest request, String path, ImageOptions options) {
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
