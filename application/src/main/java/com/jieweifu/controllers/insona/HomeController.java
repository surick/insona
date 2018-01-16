package com.jieweifu.controllers.insona;

import com.jieweifu.common.utils.ErrorUtil;
import com.jieweifu.models.Result;
import com.jieweifu.models.insona.Home;
import com.jieweifu.services.insona.HomeService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
@RestController("InsonaHome")
@RequestMapping("insona/home")
public class HomeController {
    private HomeService homeService;

    @Value("${custom.upload.home}")
    private String uploadPath;

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
     * @param home home
     * @return message
     */
    @PutMapping("updateHome")
    public Result updateHome(@Valid @RequestBody Home home) {
        if (homeService.getHomeById(home.getId()) == null)
            return new Result().setError("不存在");
        homeService.updateHome(home);
        return new Result().setMessage("修改成功");
    }

    /**
     * 删除家庭背景
     *
     * @param ids id数组
     * @return message
     */
    @DeleteMapping("removeHome")
    public Result removeHome(@RequestBody List<String> ids) {
        for (String id : ids) {
            if (homeService.getHomeById(Integer.parseInt(id)) == null) {
                return new Result().setError("无效id");
            }
            homeService.removeHome(Integer.parseInt(id));
        }
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
        if (StringUtils.isBlank(title))
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

    /**
     * 分页查询
     *
     * @param pageIndex 起始页
     * @param pageSize  条目数
     * @return map
     */
    @GetMapping("pageHome/{pageIndex}/{pageSize}")
    public Result getInfoByPage(@PathVariable("pageIndex") int pageIndex,
                                @PathVariable("pageSize") int pageSize) {
        if (pageIndex < 0 || pageSize < 0)
            return new Result().setError("页码或条目数不合法");
        List<Home> infoList = homeService.homePage(pageIndex, pageSize);
        int total = homeService.getHomeTotal();
        Map<String, Object> map = new HashMap<>();
        map.put("list", infoList);
        map.put("total", total);
        return new Result().setData(map);
    }


}
