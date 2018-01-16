package com.jieweifu.controllers.admin;

import com.jieweifu.common.utils.ErrorUtil;
import com.jieweifu.interceptors.AdminAuthAnnotation;
import com.jieweifu.models.MenuElements;
import com.jieweifu.models.Result;
import com.jieweifu.models.admin.Element;
import com.jieweifu.models.admin.Menu;
import com.jieweifu.models.admin.RoleAuthority;
import com.jieweifu.models.admin.Role;
import com.jieweifu.services.admin.*;
import com.jieweifu.vo.admin.MenuElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 用户角色接口
 */
@SuppressWarnings("unused")
@RestController("SystemRole")
@RequestMapping("sys/role")
@AdminAuthAnnotation
public class RoleController {

    private RoleService roleService;
    private RoleUserService roleUserService;
    private RoleAuthorityService roleAuthorityService;
    private UserService userService;
    private ElementService elementService;
    private MenuService menuService;

    @Autowired
    public RoleController(RoleService roleService,
                          RoleUserService roleUserService,
                          RoleAuthorityService roleAuthorityService,
                          UserService userService,
                          ElementService elementService,
                          MenuService menuService) {
        this.roleService = roleService;
        this.roleUserService = roleUserService;
        this.roleAuthorityService = roleAuthorityService;
        this.userService = userService;
        this.elementService = elementService;
        this.menuService = menuService;
    }

    /**
     * 查找全部角色
     */
    @GetMapping("listAllRoles")
    public Result listAllRoles() {
        List<Role> pRoleList = roleService.getRoleByParentId(-1);
        List<Role> roleList = new ArrayList<>();
        pRoleList.forEach(
                role -> {
                    Role role1 = getRoleTree(role.getId());
                    roleList.add(role1);
                }
        );
        roleList.remove(0);
        return new Result().setData(roleList);
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
    @DeleteMapping("removeRole")
    public Result removeRole(@RequestBody String ids) {
        int id = Integer.parseInt(ids);
        if (id <= 1)
            return new Result().setError("id不合法");
        if (roleService.getRoleById(id) == null)
            return new Result().setError("分类不存在");
        if (!roleService.getRoleByParentId(id).isEmpty())
            return new Result().setError("分类下不为空,不允许删除");
        if (roleUserService.getRoleUserByRoleId(id) != null)
            return new Result().setError("角色下用户不为空,不允许删除");
        if (roleAuthorityService.getRoleAuthorityById(roleService.getRoleById(id).getId()) != null) {
            roleAuthorityService.deleteRoleAuthority(roleService.getRoleById(id).getId());
            roleService.deleteRole(id);
        }
        return new Result().setMessage("删除角色成功");

    }

    /**
     * 添加角色
     */
    @PostMapping("saveRole")
    public Result saveRole(@Valid @RequestBody Role role, Errors errors) {
        if (errors.hasErrors()) {
            return new Result().setError(ErrorUtil.getErrors(errors));
        }
        if (role.getParentId() == 0) {
            role.setParentId(-1);
        }
        if (roleService.getRoleByName(role.getRoleName()) != null) {
            return new Result().setMessage("角色已存在");
        }
        if (roleService.getRoleById(role.getParentId()) == null && role.getParentId() != -1) {
            return new Result().setMessage("父级不存在");
        }
        roleService.addRole(role);
        return new Result().setMessage("新增成功");
    }

    /**
     * 添加权限
     */
    @PostMapping("saveAuthority/{id}")
    public Result saveAuthority(@PathVariable("id") String id,
                                @RequestBody Map<String, List<String>> mapList) {
        if (!id.matches("^[0-9]*$")) {
            return new Result().setError("id非法");
        }
        if (mapList.isEmpty())
            return new Result().setError("权限为空");
        mapList.forEach(
                (code, codes) ->
                {
                    for (String cd : codes) {
                        if (cd.startsWith("SYS")) {
                            RoleAuthority roleAuthority = new RoleAuthority();
                            Element element = elementService.getElement(cd);
                            if (roleAuthorityService.getRoleAuth("MENU", element.getMenuId(), Integer.parseInt(id)) == null) {
                                RoleAuthority roleAuthority1 = new RoleAuthority();
                                roleAuthority1.setResourceType("MENU");
                                roleAuthority1.setResourceId(element.getMenuId());
                                roleAuthority1.setRoleId(Integer.parseInt(id));
                                roleAuthorityService.addRoleAuthority(roleAuthority1);
                            }
                            if (roleAuthorityService.getRoleAuth("MENU", 1, Integer.parseInt(id)) == null
                                    && !cd.startsWith("SYS_INS")) {
                                RoleAuthority roleAuthority2 = new RoleAuthority();
                                roleAuthority2.setRoleId(Integer.parseInt(id));
                                roleAuthority2.setResourceType("MENU");
                                roleAuthority2.setResourceId(1);
                                roleAuthorityService.addRoleAuthority(roleAuthority2);
                            }
                            if (roleAuthorityService.getRoleAuth("MENU", 6, Integer.parseInt(id)) == null
                                    && cd.startsWith("SYS_INS")) {
                                RoleAuthority roleAuthority2 = new RoleAuthority();
                                roleAuthority2.setRoleId(Integer.parseInt(id));
                                roleAuthority2.setResourceType("MENU");
                                roleAuthority2.setResourceId(6);
                                roleAuthorityService.addRoleAuthority(roleAuthority2);
                            }
                            roleAuthority.setRoleId(Integer.parseInt(id));
                            roleAuthority.setResourceType("ELEMENT");
                            roleAuthority.setResourceId(element.getId());
                            roleAuthorityService.addRoleAuthority(roleAuthority);
                        } else if (cd.startsWith("AUTH")) {
                            if (roleAuthorityService.getRoleAuth("MENU", menuService.getMenuByCode(cd).getId(), Integer.parseInt(id)) == null) {
                                RoleAuthority roleAuthority1 = new RoleAuthority();
                                roleAuthority1.setResourceType("MENU");
                                roleAuthority1.setResourceId(menuService.getMenuByCode(cd).getId());
                                roleAuthority1.setRoleId(Integer.parseInt(id));
                                roleAuthorityService.addRoleAuthority(roleAuthority1);
                            }
                        }
                    }
                }
        );
        return new Result().setMessage("添加/修改权限成功");
    }

    /**
     * 修改角色权限
     */
    @PutMapping("updateAuthority/{id}")
    public Result updateAuthority(@PathVariable("id") String id, @RequestBody Map<String, List<String>> mapList) {
        roleAuthorityService.deleteRoleAuthority(Integer.parseInt(id));
        return saveAuthority(id, mapList);
    }

    /**
     * 显示角色权限
     */
    @GetMapping("getUserPower/{id}")
    @AdminAuthAnnotation(check = false)
    public Result getUserPower(@PathVariable("id") int roleId) {
        MenuController menuController = new MenuController(menuService, elementService);
        //获取角色权限
        MenuElements menuElements = roleAuthorityService.getMenuElements(roleId);
        //获取所有权限
        List<Menu> list = menuController.getAll();
        //复制所有权限模板
        List<Menu> menuList = new ArrayList<>();
        //权限设置->auth1
        Menu auth1 = list.get(0);
        //英索纳管理->auth2
        Menu auth2 = list.get(1);
        //获取权限设置
        menuList.add(get(auth1, menuElements));
        //获取英索纳管理
        menuList.add(get(auth2, menuElements));

        return new Result().setData(menuList);
    }

    //判断方法
    public Menu get(Menu auth, MenuElements menuElements) {
        Menu cAuth = auth;
        for (int i = 0; i < auth.getChildren().size(); i++) {
            //遍历菜单按钮
            for (int j = 0; j < auth.getChildren().get(i).getChildren().size(); j++) {
                //遍历已有权限
                for (int k = 0; k < menuElements.getElements().size(); k++) {
                    //设置已有权限按钮为true
                    if (auth.getChildren().get(i).getChildren().get(j).getCode().equals(menuElements.getElements().get(k).getCode())) {
                        cAuth.getChildren().get(i).getChildren().get(j).setChecked(true);
                    }
                }
            }
        }
        return cAuth;
    }
}
