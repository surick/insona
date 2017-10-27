package com.jieweifu.controllers.admin;

import com.jieweifu.interceptors.AdminAuthAnnotation;
import com.jieweifu.models.ResultModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@SuppressWarnings("unused")
@Controller("SystemGroup")
@RequestMapping("sys/group")
@AdminAuthAnnotation
public class GroupController {

    @GetMapping("all")
    @ResponseBody
    public ResultModel getGroups() {
        return null;
    }

    @PutMapping("update/{groupId}")
    @ResponseBody
    public ResultModel updateGroup(@PathVariable("groupId") int groupId, @RequestBody Map<String, Object> groupInfo) {
        return null;
    }

    @PostMapping("new")
    @ResponseBody
    public ResultModel addGroup(@RequestBody Map<String, Object> groupInfo) {
        return null;
    }

    @DeleteMapping("delete/{groupId}")
    @ResponseBody
    public ResultModel deleteGroup(@PathVariable("groupId") int groupId) {
        return null;
    }
}
