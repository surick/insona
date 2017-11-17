package com.jieweifu.controllers.GizWits;

import com.jieweifu.common.utils.ErrorUtil;
import com.jieweifu.common.utils.FileUtil;
import com.jieweifu.models.Result;
import com.jieweifu.models.gizWits.Home;
import com.jieweifu.services.gizWits.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @PostMapping("imgUpload")
    public Result imgUpload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {

        String contentType = file.getContentType();
        String fileName = file.getOriginalFilename();
        System.out.println(contentType + "  " + fileName);
        String filePath = request.getSession().getServletContext().getRealPath("D:\\Insona_java\\application\\src\\main\\java\\com\\jieweifu\\imgUpload\\");
        try {

            FileUtil.uploadFile(file.getBytes(), filePath, fileName);
            System.out.println(111);
        } catch (Exception e) {
            return new Result().setError("上传失败");
        }
        return new Result().setMessage("长传成功");
    }
}
