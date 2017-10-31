package com.jieweifu.controllers.admin;

import com.jieweifu.interceptors.AdminAuthAnnotation;
import com.jieweifu.models.Result;
import com.jieweifu.models.admin.RoleUser;
import com.jieweifu.models.admin.User;
import com.jieweifu.services.admin.RoleService;
import com.jieweifu.services.admin.RoleUserService;
import com.jieweifu.services.admin.UserService;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public UserController(UserService userService,RoleService roleService,RoleUserService roleUserService) {
        this.userService = userService;
        this.roleService = roleService;
        this.roleUserService = roleUserService;
    }

    @GetMapping("user")
    public Result getAllUsers(){
        return new Result().setData(userService.getAllUsers());
    }

    /**
     * 根据名称查找用户
     * @param userName
     * @return
     */
    @GetMapping("getUserByUserName/{userName}")
    @AdminAuthAnnotation(check = false)
    public Result getUserByUserName(@PathVariable("userName") String userName){
        if(userName==null)
            throw new RuntimeException("查询信息不能为空");
        User user = userService.getUserByUserName(userName);
        if(user==null)
            throw new RuntimeException("用户不存在");
        return new Result().setData(user);
    }

    /**
     * 新增用户
      * @param user
     * @return
     */
    @PostMapping("addUser")
    @AdminAuthAnnotation(check = false)
    public Result addUser(@RequestBody User user){
        if(user.getName()==null||user.getUserName()==null||
                user.getEmail()==null||user.getPassword()==null)
            throw new RuntimeException("缺省信息不可为空");
        if(userService.getUserByUserName(user.getUserName())!=null)
            throw new RuntimeException("用户名已存在");
        userService.addUser(user);
        User user1 = userService.getUserByUserName(user.getUserName());
        userService.updateUserPassword(user1.getId(),user.getPassword());
        return new Result().setMessage("新增成功");
    }


    /**
     * 修改用户
     * @param user
     * @return
     */
    @PutMapping("updateUser")
    @AdminAuthAnnotation(check = false)
    public Result updateUsers(@RequestBody User user){
        if(user.getUserName()==null||user.getName()==null)
            throw new RuntimeException("用户名不能为空");
        userService.updateUser(user);
        return new Result().setMessage("修改成功");
    }

    /**
     * 删除用户
     * @param id
     * @return
     */
    @DeleteMapping("deleteUser/{id}")
    @AdminAuthAnnotation(check = false)
    public Result deleteUser(@PathVariable("id") int id){
        if(id==0||userService.getUserById(id)==null)
            throw new RuntimeException("用户不存在");
        userService.deleteUser(id);
        return new Result().setMessage("删除成功");

    }



    /**
     * 分页查询用户
     * @param pageIndex
     * @return
     */
    @GetMapping("pageUser/{pageIndex}/{pageSize}")
    @AdminAuthAnnotation(check = false)
    public Result getUserByPage(@PathVariable("pageIndex") int pageIndex,
                                @PathVariable("pageSize") int pageSize){
        if(pageIndex<1)
            throw new RuntimeException("页码不合法");
        List<User> userList = userService.getUsersByPage(pageIndex,pageSize);
        return new Result().setData(userList);
    }

    /**
     * 配置用户角色
     * @param roleInfo
     * @return
     */
    @PutMapping("updateRoleUser")
    @AdminAuthAnnotation(check = false)
    public Result updateRoleUser(@RequestBody RoleInfo roleInfo){
        boolean flag = false;
        if(roleInfo.getUserId()!=0 && roleInfo.getRoleId()!=0){
            roleUserService.deleteRoleUser(roleInfo.getUserId());
                RoleUser roleUser = new RoleUser();
                roleUser.setUserId(roleInfo.getUserId());
                roleUser.setRoleId(roleInfo.getRoleId());
                roleUser.setDescription(roleService.getRoleById(roleInfo.getRoleId()).getDescription());
                roleUserService.addRoleUser(roleUser);
                flag = true;
            }


        return new Result().setMessage(flag?"配置角色成功":"配置角色失败");
    }

    public static class RoleInfo {
        @NotEmpty(message = "userId不合法")
        private int userId;
        @NotEmpty(message = "角色不能为空")
        private int roleId;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public int getRoleId() {
            return roleId;
        }

        public void setRoleId(int roleId) {
            this.roleId = roleId;
        }
    }


}
