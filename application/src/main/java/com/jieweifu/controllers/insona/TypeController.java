package com.jieweifu.controllers.insona;

import com.jieweifu.common.utils.ErrorUtil;
import com.jieweifu.interceptors.AdminAuthAnnotation;
import com.jieweifu.models.Result;
import com.jieweifu.models.insona.Type;
import com.jieweifu.services.insona.TypeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
@RestController("InsonaType")
@RequestMapping("insona/type")
@AdminAuthAnnotation
public class TypeController {

    private TypeService typeService;

    @Autowired
    public TypeController(TypeService typeService) {
        this.typeService = typeService;
    }

    /**
     * 新增设备类型
     */
    @PostMapping("save")
    public Result saveType(@Valid @RequestBody Type type, Errors errors) {
        System.out.println(type.getType_id());
        if (errors.hasErrors()) {
            return new Result().setError(ErrorUtil.getErrors(errors));
        }
        try {
            typeService.saveType(type);
        } catch (Exception e) {
            return new Result().setError(500, "系统繁忙，请刷新后重试");
        }
        return new Result().setMessage("保存成功");
    }
    /**
     * 新增类型批次
     */
    @PostMapping("new")
    public Result newType(@Valid @RequestBody Type type, Errors errors) {
        if (errors.hasErrors()) {
            return new Result().setError(ErrorUtil.getErrors(errors));
        }
        int batch = typeService.newTotal(type.getType_name());
        if(batch>0)
            type.setBatch(String.valueOf(batch+1));
        try {
            typeService.saveType(type);
        } catch (Exception e) {
            return new Result().setError(500, "系统繁忙，请刷新后重试");
        }
        return new Result().setMessage("新增批次成功");
    }

    /**
     * 删除设备类型
     */
    @DeleteMapping("remove")
    public Result removeType(@RequestBody List<String> ids) {
        System.out.println("ids" + ids.size());
        if (ids.isEmpty()) {
            return new Result().setError("id为空");
        }
        for (String id : ids) {
            typeService.removeType(id);
        }
        return new Result().setMessage("删除成功");
    }

    /**
     * 分页获取所有类型
     */
    @GetMapping("list/{pageIndex}/{pageSize}")
    public Result listTypes(@PathVariable("pageIndex") int pageIndex,
                            @PathVariable("pageSize") int pageSize) {
        if (pageIndex < 0 || pageSize < 0) {
            return new Result().setError("页码或条目数不合法");
        }
        List<Type> list = null;
        int total = 0;
        try {
            list = typeService.listTypes(pageIndex, pageSize);
            total = typeService.getTotal();
        } catch (Exception e) {
            return new Result().setError("获取列表失败，请刷新后重试");
        }
        System.out.println(list.get(0).getId());
        Map<String, Object> map = new HashMap<>();
        map.put("list", list);
        map.put("total", total);
        return new Result().setData(map);
    }

    /**
     * 修改数据类型
     */
    @PutMapping("update")
    public Result updateType(@Valid @RequestBody Type type, Errors errors) {
        if (errors.hasErrors()) {
            return new Result().setError(ErrorUtil.getErrors(errors));
        }
        try {
            typeService.updateType(type);
        } catch (Exception e) {
            return new Result().setError("修改失败，请刷新后重试");
        }
        return new Result().setMessage("修改成功");
    }

    /**
     * 根据type_id获取类型
     */
    @GetMapping("get/{type_id}")
    public Result getType(@PathVariable("type_id") String type_id) {
        if (StringUtils.isBlank(type_id)) {
            return new Result().setError("id有误");
        }
        Type type = null;
        try {
            type = typeService.getTypeById(type_id);
        } catch (Exception e) {
            return new Result().setError("系统繁忙，请刷新后重试");
        }
        return new Result().setData(type);
    }

    /**
     * 获取设备类型
     */
    @GetMapping("types")
    public Result types(){
        List<Type> list;
        try{
            list = typeService.types();
        }catch (Exception e){
            return new Result().setError("获取设备类型错误");
        }
        return new Result().setData(list);
    }
}
