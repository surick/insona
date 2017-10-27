package com.jieweifu.controllers.admin;

import com.jieweifu.interceptors.AdminAuthAnnotation;
import com.jieweifu.models.ResultModel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    public ResultModel updateGroup(@PathVariable("groupId") int groupId, String groupName, int orderNum, String description, boolean enabled) {
        return null;
    }

    @PostMapping("new")
    @ResponseBody
    public ResultModel addGroup(String groupName, int orderNum, String description, boolean enabled) {
        return null;
    }

    @DeleteMapping("delete/{groupId}")
    @ResponseBody
    public ResultModel deleteGroup(@PathVariable("groupId") int groupId) {
        return null;
    }
}
