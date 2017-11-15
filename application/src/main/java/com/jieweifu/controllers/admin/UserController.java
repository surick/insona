package com.jieweifu.controllers.admin;

import com.jieweifu.common.utils.ErrorUtil;
import com.jieweifu.interceptors.AdminAuthAnnotation;
import com.jieweifu.models.Result;
import com.jieweifu.models.admin.RoleUser;
import com.jieweifu.models.admin.User;
import com.jieweifu.models.regex.Regex;
import com.jieweifu.services.admin.RoleService;
import com.jieweifu.services.admin.RoleUserService;
import com.jieweifu.services.admin.UserService;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.*;

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
    @GetMapping("getUserByName/{Name}")
    public Result getUserByUserName(@PathVariable("Name") String Name) {
        if (Name == null)
            return new Result().setError("查询信息不能为空");
        List<User> user = userService.getUserByName(Name);
        if (user == null)
            return new Result().setError("用户不存在");
        return new Result().setData(user);
    }

    /**
     * 新增用户
     */
    @PostMapping("saveUser")
    public Result saveUser(@Valid @RequestBody User user, Errors errors) {
        if (errors.hasErrors()) {
            return new Result().setError(ErrorUtil.getErrors(errors));
        }
        if (!user.getUserName().matches(Regex.USERNAME_REX) ||
                !user.getPassword().matches(Regex.PASSWORD_REX))
            return new Result().setError("账户名4-16位!密码6-16位!");
        if (userService.getUserByUserName(user.getUserName()) != null)
            return new Result().setError("用户名已存在");
        userService.addUser(user);
        return new Result().setMessage("新增成功");
    }


    /**
     * 修改用户
     */
    @PutMapping("updateUser")
    public Result updateUsers(@Valid @RequestBody User user, Errors errors) {
        if (user.getId() == -1)
            return new Result().setError("非法id");
        if (errors.hasErrors()) {
            return new Result().setError(ErrorUtil.getErrors(errors));
        }
        if (userService.getUserById(user.getId()) == null)
            return new Result().setError("用户不存在");
        if (user.getUserName() != null || user.getPassword() != null)
            return new Result().setError("登录名密码不允许更改");
        userService.updateUser(user);
        return new Result().setMessage("修改成功");
    }

    /**
     * 删除用户
     */
    @DeleteMapping("deleteUser")
    public Result deleteUser(@RequestBody List<String> ids) {
        for (String id : ids) {
            if (userService.getUserById(Integer.parseInt(id)) == null || id.equals("1")) {
                return new Result().setError("无效id");
            }
            userService.isDelete(Integer.parseInt(id));
        }
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
        for(String roleIds : roleInfo.getRoleId()){
            Integer roleId = Integer.parseInt(roleIds);
            if (userService.getUserById(roleInfo.getUserId()) != null &&
                    roleService.getRoleById(roleId) != null){
                flag = true;
            }else {
                flag = false;
            }
        }
        if(flag){
            roleUserService.deleteRoleUser(roleInfo.getUserId());
            for (String roleIds : roleInfo.getRoleId()) {
                Integer roleId = Integer.parseInt(roleIds);
                if (userService.getUserById(roleInfo.getUserId()) != null &&
                        roleService.getRoleById(roleId) != null) {
                    RoleUser roleUser = new RoleUser();
                    roleUser.setUserId(roleInfo.getUserId());
                    roleUser.setRoleId(roleId);
                    roleUser.setDescription(roleService.getRoleById(roleId).getDescription());
                    roleUserService.addRoleUser(roleUser);
                    flag = true;
                }
            }
            return new Result().setMessage("配置角色成功");
        }else {
            return new Result().setError("配置权限失败");
        }
    }

    public static class RoleInfo {

        private Integer userId;

        private List<String> roleId;

        @Min(value = 0, message = "userId不合法")
        public Integer getUserId() {
            return userId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        @NotEmpty(message = "roleId不能为空")
        public List<String> getRoleId() {
            return roleId;
        }

        public void setRoleId(List<String> roleId) {
            this.roleId = roleId;
        }
    }


}
