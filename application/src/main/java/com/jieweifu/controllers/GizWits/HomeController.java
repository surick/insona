package com.jieweifu.controllers.GizWits;

import com.jieweifu.common.utils.ErrorUtil;
import com.jieweifu.models.Result;
import com.jieweifu.models.gizWits.Home;
import com.jieweifu.services.gizWits.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@SuppressWarnings("unused")
@RestController("GizWitsHome")
@RequestMapping("giz/home")
public class HomeController {
    private HomeService homeService;

    @Autowired
    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }

    /**
     * 新增家庭背景
     *
     * @param home   home
     * @param errors 判断字段
     * @return message
     */
    @PostMapping("saveHome")
    public Result saveHome(@Valid @RequestBody Home home, Errors errors) {
        if (errors.hasErrors())
            return new Result().setError(ErrorUtil.getErrors(errors));
        if (homeService.getHomeByTitle(home.getTitle()) != null)
            return new Result().setError("已存在");
        homeService.saveHome(home);
        return new Result().setMessage("新增成功");
    }

    /**
     * 修改家庭背景
     *
     * @param home   home
     * @param errors 判断字段
     * @return message
     */
    @PutMapping("updateHome")
    public Result updateHome(@Valid @RequestBody Home home, Errors errors) {
        if (errors.hasErrors())
            return new Result().setError(ErrorUtil.getErrors(errors));
        if (homeService.getHomeById(home.getId()) == null)
            return new Result().setError("不存在");
        homeService.updateHome(home);
        return new Result().setMessage("修改成功");
    }

    /**
     * 删除家庭背景
     *
     * @param id id
     * @return message
     */
    @DeleteMapping("removeHome")
    public Result removeHome(@Param("id") Integer id) {
        if (id == 0)
            return new Result().setError("id能为空");
        if (homeService.getHomeById(id) == null)
            return new Result().setError("不存在");
        homeService.removeHome(id);
        return new Result().setMessage("删除成功");
    }

    /**
     * 根据title查找
     *
     * @param title title
     * @return home
     */
    @GetMapping("AllHomeByTitle")
    public Result AllHomeByTitle(@Param("title") String title) {
        if (title.length() < 1)
            return new Result().setError("title不能为空");
        Home home = homeService.getHomeByTitle(title);
        return new Result().setData(home);
    }

    /**
     * 查询所有home
     *
     * @return list
     */
    @GetMapping("AllHome")
    public Result AllHome() {
        List<Home> homeList = homeService.AllHome();
        return new Result().setData(homeList);
    }
}
