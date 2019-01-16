package com.jieweifu.controllers.insona;

import com.jieweifu.common.utils.ErrorUtil;
import com.jieweifu.interceptors.AdminAuthAnnotation;
import com.jieweifu.models.Result;
import com.jieweifu.models.insona.Project;
import com.jieweifu.services.insona.JobService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author Jin
 * @date 2018/9/28下午1:54
 */
@SuppressWarnings("unused")
@RestController("eApiJob")
@RequestMapping("eApi/job")
public class JobController {
    private JobService jobService;

    @Autowired
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @AdminAuthAnnotation(check = false)
    @PostMapping("pushProject")
    public Result pushProject(@Valid @RequestBody Project project, Errors errors) {
        if (errors.hasErrors()) {
            return new Result().setError(ErrorUtil.getErrors(errors));
        }
        try {
            jobService.pushProject(project);
        } catch (Exception e) {
            return new Result().setError("系统繁忙，请刷新后重试");
        }
        return new Result().setMessage("操作Project成功");
    }

    @AdminAuthAnnotation(check = false)
    @GetMapping("getProject")
    public Result getProject(@RequestParam(value = "did", required = false) String did,
                             @RequestParam(value = "mac", required = false) String mac) {
        if (StringUtils.isBlank(did) && StringUtils.isBlank(mac)) {
            return new Result().setError("查询信息不能为空");
        }
        Project project = jobService.getProject(did, mac);
        if (null == project) {
            return new Result().setError("Project不存在");
        }
        return new Result().setData(project);

    }
}
