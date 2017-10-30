package com.jieweifu.controllers.admin;

import com.jieweifu.interceptors.AdminAuthAnnotation;
import com.jieweifu.models.Result;
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
    public Result getGroups() {
        return null;
    }

    @PutMapping("update/{groupId}")
    public Result updateGroup(@PathVariable("groupId") int groupId, @RequestBody Map<String, Object> groupInfo) {
        return null;
    }

    @PostMapping("new")
    public Result addGroup(@RequestBody Map<String, Object> groupInfo) {
        return null;
    }

    @DeleteMapping("delete/{groupId}")
    public Result deleteGroup(@PathVariable("groupId") int groupId) {
        return null;
    }
}
