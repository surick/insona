package com.jieweifu.controllers.admin;

import com.jieweifu.common.utils.ErrorUtil;
import com.jieweifu.interceptors.AdminAuthAnnotation;
import com.jieweifu.models.Result;
import com.jieweifu.models.admin.RoleUser;
import com.jieweifu.models.admin.User;
import com.jieweifu.services.admin.RoleService;
import com.jieweifu.services.admin.RoleUserService;
import com.jieweifu.services.admin.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理用户类
 */
@SuppressWarnings("unused")
@RestController("SystemUser")
@RequestMapping("sys/user")
@AdminAuthAnnotation
public class UserController {
    private UserService userService;
    private RoleService roleService;
    private RoleUserService roleUserService;

    @Autowired
    public UserController(UserService userService, RoleService roleService, RoleUserService roleUserService) {
        this.userService = userService;
        this.roleService = roleService;
        this.roleUserService = roleUserService;
    }

    @GetMapping("user")
    public Result getAllUsers() {
        return new Result().setData(userService.getAllUsers());
    }

    /**
     * 根据名称查找用户
     */
    @GetMapping("getUserByUserName/{userName}")
    public Result getUserByUserName(@PathVariable("userName") String userName) {
        if (userName == null)
            return new Result().setError("查询信息不能为空");
        User user = userService.getUserByUserName(userName);
        if (user == null)
            return new Result().setError("用户不存在");
        return new Result().setData(user);
    }

    /**
     * 新增用户
     */
    @PostMapping("saveUser")
    public Result saveUser(@RequestBody User user) {
        if (user.getUserName() == null || user.getPassword() == null||
                user.getUserName() == "" || user.getPassword() == "")
            return new Result().setError("账户名密码不允许为空");
        if (userService.getUserByUserName(user.getUserName()) != null)
            return new Result().setError("用户名已存在");
        userService.addUser(user);
        return new Result().setMessage("新增成功");
    }


    /**
     * 修改用户
     */
    @PutMapping("updateUser")
    public Result updateUsers(@RequestBody User user) {
        if (userService.getUserById(user.getId()) == null)
            return new Result().setError("用户不存在");
        userService.updateUser(user);
        return new Result().setMessage("修改成功");
    }

    /**
     * 删除用户
     */
    @DeleteMapping("deleteUser/{id}")

    public Result deleteUser(@PathVariable("id") int id) {
        if (id < 1 || userService.getUserById(id) == null)
            return new Result().setError("用户不存在");
        userService.isDelete(id);
        return new Result().setMessage("删除成功");

    }


    /**
     * 分页查询用户
     */
    @GetMapping("pageUser/{pageIndex}/{pageSize}")
    public Result getUserByPage(@PathVariable("pageIndex") int pageIndex,
                                @PathVariable("pageSize") int pageSize) {
        if (pageIndex < 0 || pageSize < 0)
            return new Result().setError("页码或条目数不合法");
        List<User> userList = userService.getUsersByPage(pageIndex, pageSize);
        int total = userService.getUserTotal();
        Map<String, Object> map = new HashMap<>();
        map.put("list", userList);
        map.put("total", total);
        return new Result().setData(map);
    }

    /**
     * 配置用户角色
     */
    @PutMapping("updateRoleUser")
    public Result updateRoleUser(@Valid @RequestBody RoleInfo roleInfo, Errors errors) {
        if (errors.hasErrors()) {
            return new Result().setError(ErrorUtil.getErrors(errors));
        }
        boolean flag = false;
        if (roleInfo.getUserId() != 0 && roleInfo.getRoleId() != 0) {
            roleUserService.deleteRoleUser(roleInfo.getUserId());
            RoleUser roleUser = new RoleUser();
            roleUser.setUserId(roleInfo.getUserId());
            roleUser.setRoleId(roleInfo.getRoleId());
            roleUser.setDescription(roleService.getRoleById(roleInfo.getRoleId()).getDescription());
            roleUserService.addRoleUser(roleUser);
            flag = true;
        }


        return new Result().setMessage(flag ? "配置角色成功" : "配置角色失败");
    }

    public static class RoleInfo {

        private int userId;

        private int roleId;

        @Min(value = 0, message = "userId不合法")
        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        @Min(value = 0, message = "角色不能为空")
        public int getRoleId() {
            return roleId;
        }

        public void setRoleId(int roleId) {
            this.roleId = roleId;
        }
    }


}
