package com.jieweifu.controllers.insona;

import com.jieweifu.models.Result;
import com.jieweifu.models.insona.About;
import com.jieweifu.services.insona.AboutService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Jin
 * @date 2018/12/10
 */
@RestController
@RequestMapping("insona/about")
public class AboutController {
    private static final Logger logger = LoggerFactory.getLogger(AboutController.class);

    private AboutService aboutService;

    @Autowired
    public AboutController(AboutService aboutService) {
        this.aboutService = aboutService;
    }

    @PostMapping("updateAbout")
    public Result updateAbout(@RequestBody About about) {
        try {
            aboutService.updateAbout(about);
            return new Result().setMessage("修改成功");
        } catch (Exception e) {
            logger.error("update about error-->{}", e);
        }
        return new Result().setError("修改失败");
    }

    @GetMapping("getAbout")
    public Result getAbout(@RequestParam(value = "id") Integer id) {
        About about = new About();
        try {
            about = aboutService.getAbout(id);
            return new Result().setData(about);
        } catch (Exception e) {
            logger.error("get about error-->{}", e);
        }
        return new Result().setError("获取失败");
    }
}
