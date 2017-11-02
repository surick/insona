package com.jieweifu.controllers.admin;

import com.jieweifu.common.utils.ErrorUtil;
import com.jieweifu.interceptors.AdminAuthAnnotation;
import com.jieweifu.models.Result;
import com.jieweifu.models.admin.RoleAuthority;
import com.jieweifu.models.admin.Role;
import com.jieweifu.services.admin.RoleAuthorityService;
import com.jieweifu.services.admin.RoleService;
import com.jieweifu.services.admin.RoleUserService;
import com.jieweifu.services.admin.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
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
    @GetMapping("listAllRoles")
    public Result listAllRoles() {
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
    public Result updateRole(@Valid @RequestBody Role role, Errors errors) {
        if (errors.hasErrors()) {
            return new Result().setError(ErrorUtil.getErrors(errors));
        }
        int row = 0;
        if (roleService.getRoleById(role.getId()) == null || role.getId() == -1) {
            return new Result().setError("id不合法");
        }
        row = roleService.updateRole(role);
        return new Result().setMessage(row != 0 ? "更新成功" : "更新失败");
    }

    /**
     * 删除角色,超级管理员不允许删除,
     * 分类不存在删除失败,
     * 分类下有子分类不允许删除,
     * 角色下有用户在使用,不允许删除
     */
    @DeleteMapping("removeRole/{id}")
    public Result removeRole(@PathVariable("id") int id) {
        if (id <= 1)
            return new Result().setError("id不合法");
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
    @PostMapping("saveRole")
    public Result saveRole(@Valid @RequestBody Role Role, Errors errors) {
        if (errors.hasErrors()) {
            return new Result().setError(ErrorUtil.getErrors(errors));
        }
        roleService.addRole(Role);
        return new Result().setMessage("新增成功");
    }


    /**
     * 添加权限
     */
    @PostMapping("saveAuthority/{id}")
    public Result saveAuthority(@PathVariable("id") int id,
                                @RequestBody Map<String, List<Integer>> mapList) {
        if (mapList.isEmpty())
            return new Result().setError("权限为空");
        mapList.forEach(
                (s, integers) ->
                {
                    for (Integer j : integers) {
                        RoleAuthority roleAuthority = new RoleAuthority();
                        roleAuthority.setRoleId(id);
                        roleAuthority.setResourceId(j);
                        roleAuthority.setResourceType(s);
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
    public Result updateAuthority(@PathVariable("id") int id, @RequestBody Map<String, List<Integer>> mapList) {
        roleAuthorityService.deleteRoleAuthority(id);
        return saveAuthority(id, mapList);
    }

    /**
     * 显示角色权限
     */
    @GetMapping("getUserPower/{id}")
    public Result getUserPower(@PathVariable("id") int roleId) {
        return new Result().setData(roleAuthorityService.getMenuElements(roleId));
    }

}
