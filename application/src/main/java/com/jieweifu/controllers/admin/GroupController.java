package com.jieweifu.controllers.admin;

import com.jieweifu.interceptors.AdminAuthAnnotation;
import com.jieweifu.models.ResultModel;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 管理组类
 */
@SuppressWarnings("unused")
@RestController("SystemGroup")
@RequestMapping("sys/group")
@AdminAuthAnnotation
public class GroupController {

    @GetMapping("all")
    public ResultModel getGroups() {
        return null;
    }

    @PutMapping("update/{groupId}")
    public ResultModel updateGroup(@PathVariable("groupId") int groupId, @RequestBody Map<String, Object> groupInfo) {
        return null;
    }

    @PostMapping("new")
    public ResultModel addGroup(@RequestBody Map<String, Object> groupInfo) {
        return null;
    }

    @DeleteMapping("delete/{groupId}")
    public ResultModel deleteGroup(@PathVariable("groupId") int groupId) {
        return null;
    }
}
