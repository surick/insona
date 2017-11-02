package com.jieweifu.controllers.admin;

import com.jieweifu.interceptors.AdminAuthAnnotation;
import com.jieweifu.models.Result;
import com.jieweifu.models.admin.RoleAuthority;
import com.jieweifu.models.admin.Role;
import com.jieweifu.services.admin.RoleAuthorityService;
import com.jieweifu.services.admin.RoleService;
import com.jieweifu.services.admin.RoleUserService;
import com.jieweifu.services.admin.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
@RestController("SystemRole")
@RequestMapping("sys/role")
@AdminAuthAnnotation
public class RoleController {

    private RoleService roleService;
    private RoleUserService roleUserService;
    private RoleAuthorityService roleAuthorityService;
    private UserService userService;

    @Autowired
    public RoleController(RoleService roleService,
                          RoleUserService roleUserService,
                          RoleAuthorityService roleAuthorityService,
                          UserService userService) {
        this.roleService = roleService;
        this.roleUserService = roleUserService;
        this.roleAuthorityService = roleAuthorityService;
        this.userService = userService;
    }

    /**
     * 查找全部角色
     */
    @GetMapping("getAllRoles")
    public Result getAllRoles() {
        return new Result().setData(getRoleTree(1));
    }

    /**
     * 角色信息Tree生成方法
     */
    private Role getRoleTree(int cid) {
        Role pRole = roleService.getRoleById(cid);
        List<Role> cRoleList = roleService.getRoleByParentId(pRole.getId());
        for (Role Role : cRoleList) {
            Role m = getRoleTree(Role.getId());
            pRole.getChildren().add(m);
        }
        return pRole;
    }

    /**
     * 根据id查找角色
     */
    @GetMapping("getRoleById/{id}")
    public Result getRoleById(@PathVariable("id") int id) {
        if (id < 1)
            return new Result().setError("id不合法");
        Role Role = roleService.getRoleById(id);
        if (Role == null)
            return new Result().setError("查找的数据不存在");
        return new Result().setData(Role);
    }

    /**
     * 更新角色
     */
    @PutMapping("updateRole")
    public Result updateRole(@RequestBody Role role) {
        int row = 0;
        if (roleService.getRoleById(role.getId()) != null
                && role.getRoleName() != null
                && role.getRoleCode() != null) {
            row = roleService.updateRole(role);
        }
        return new Result().setMessage(row != 0 ? "更新成功" : "更新失败");
    }

    /**
     * 删除角色,超级管理员不允许删除,
     * 分类不存在删除失败,
     * 分类下有子分类不允许删除,
     * 角色下有用户在使用,不允许删除
     */
    @DeleteMapping("deleteRole/{id}")
    public Result deleteRole(@PathVariable("id") int id) {
        if (id == 1)
            return new Result().setError("超级管理员不允许删除");
        if (roleService.getRoleById(id) == null)
            return new Result().setError("分类不存在");
        if (!roleService.getRoleByParentId(id).isEmpty())
            return new Result().setError("分类下不为空,不允许删除");
        if (roleUserService.getRoleUserByRoleId(id) != null)
            return new Result().setError("角色下用户不为空,不允许删除");
        roleService.deleteRole(id);
        roleAuthorityService.deleteRoleAuthority(roleService.getRoleById(id).getId());
        return new Result().setMessage("删除角色成功");

    }

    /**
     * 添加角色
     */
    @PostMapping("addRole")
    public Result addRole(@RequestBody Role Role) {
        boolean flag = true;
        if (roleService.getRoleById(Role.getId()) == null
                && Role.getRoleName() != null
                && Role.getRoleCode() != null) {
            roleService.addRole(Role);
        } else {
            flag = false;
        }
        return new Result().setMessage(flag ? "新增成功" : "新增失败");
    }


    /**
     * 添加权限
     */
    @PostMapping("addAuthority/{id}")
    public Result addAuthority(@PathVariable("id") int id, @RequestBody Map<Integer, List<String>> mapList) {
        if (mapList.isEmpty())
            return new Result().setError("权限为空");
        mapList.forEach(
                (i, strings) ->
                {
                    for (String j : strings) {
                        RoleAuthority roleAuthority = new RoleAuthority();
                        roleAuthority.setRoleId(id);
                        roleAuthority.setResourceId(i);
                        roleAuthority.setResourceType(j);
                        roleAuthorityService.addRoleAuthority(roleAuthority);
                    }
                }
        );

        return new Result().setMessage("添加权限成功");
    }

    /**
     * 修改角色权限
     */
    @PutMapping("updateAuthority/{id}")
    public Result updateAuthority(@PathVariable("id") int id, @RequestBody Map<Integer, List<String>> mapList) {
        roleAuthorityService.deleteRoleAuthority(id);
        return addAuthority(id, mapList);
    }

    /**
     * 显示角色权限
     */
    @GetMapping("getUserPower/{id}")
    public Result getUserPower(@PathVariable("id") int roleId) {
        return new Result().setData(roleAuthorityService.getMenuElements(roleId));
    }

}
