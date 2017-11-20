package com.jieweifu.controllers.other;

import com.jieweifu.common.utils.ErrorUtil;
import com.jieweifu.models.Result;
import com.jieweifu.models.gizWits.Info;
import com.jieweifu.services.gizWits.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * 分页查询
     *
     * @param pageIndex 起始页
     * @param pageSize  条目数
     * @return map
     */
    @GetMapping("pageInfo/{pageIndex}/{pageSize}")
    public Result getInfoByPage(@PathVariable("pageIndex") int pageIndex,
                                @PathVariable("pageSize") int pageSize) {
        if (pageIndex < 0 || pageSize < 0)
            return new Result().setError("页码或条目数不合法");
        List<Info> infoList = infoService.pageInfo(pageIndex, pageSize);
        int total = infoService.getInfoTotal();
        Map<String, Object> map = new HashMap<>();
        map.put("list", infoList);
        map.put("total", total);
        return new Result().setData(map);
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
     * @param ids id数组
     * @return message
     */
    @DeleteMapping("removeInfo")
    public Result removeInfo(@RequestBody List<String> ids) {
        for (String id : ids) {
            if (infoService.getInfoById(Integer.parseInt(id)) == null) {
                return new Result().setError("无效id");
            }
            infoService.removeInfo(Integer.parseInt(id));
        }
        return new Result().setMessage("删除成功");
    }
}
