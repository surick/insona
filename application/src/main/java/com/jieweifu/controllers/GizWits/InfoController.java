package com.jieweifu.controllers.GizWits;

import com.jieweifu.common.utils.ErrorUtil;
import com.jieweifu.models.Result;
import com.jieweifu.models.gizWits.Info;
import com.jieweifu.services.gizWits.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@SuppressWarnings("unused")
@RestController("GizWitsInfo")
@RequestMapping("giz/info")
public class InfoController {

    private InfoService infoService;

    @Autowired
    public InfoController(InfoService infoService) {
        this.infoService = infoService;
    }

    /**
     * 查询全部信息
     *
     * @return list
     */
    @GetMapping("listAllInfo")
    public Result listAllInfo() {
        List<Info> list = infoService.getInfo();
        return new Result().setData(list);
    }

    /**
     * 按title查找info
     *
     * @param title 标题
     * @return list
     */
    @GetMapping("InfoByTitle")
    public Result InfoByTitle(@Param("title") String title) {
        if (title.length() < 1)
            return new Result().setError("title不能为空");
        Info info = infoService.getInfoByTitle(title);
        return new Result().setData(info);
    }

    /**
     * 按type查找info
     *
     * @param type 类型
     * @return list
     */
    @GetMapping("InfoByType")
    public Result InfoByType(@Param("type") Integer type) {
        if (type == null) {
            return new Result().setError("type不能为空");
        }
        List<Info> infoList = infoService.getInfoByType(type);
        return new Result().setData(infoList);
    }

    /**
     * 编辑info
     *
     * @param info   info
     * @param errors 判断字段
     * @return message
     */
    @PutMapping("updateInfo")
    public Result updateInfo(@Valid @RequestBody Info info, Errors errors) {
        if (errors.hasErrors())
            return new Result().setError(ErrorUtil.getErrors(errors));
        if (infoService.getInfoById(info.getId()) == null)
            return new Result().setError("不存在");
        infoService.updateInfo(info);
        return new Result().setMessage("修改成功");
    }

    /**
     * 新增info
     *
     * @param info   新增
     * @param errors 判断字段
     * @return message
     */
    @PostMapping("saveInfo")
    public Result saveInfo(@Valid @RequestBody Info info, Errors errors) {
        if (errors.hasErrors())
            return new Result().setError(ErrorUtil.getErrors(errors));
        if (infoService.getInfoByTitle(info.getTitle()) != null)
            return new Result().setError("已存在");
        infoService.saveInfo(info);
        return new Result().setMessage("新增成功");
    }

    /**
     * 删除info
     *
     * @param id id
     * @return message
     */
    @DeleteMapping("removeInfo")
    public Result removeInfo(@Param("id") Integer id) {
        if (id == 0)
            return new Result().setError("id不能为空");
        if (infoService.getInfoById(id) == null)
            return new Result().setError("不存在");
        infoService.removeInfo(id);
        return new Result().setMessage("删除成功");
    }
}
