package com.jieweifu.controllers.admin;

import com.jieweifu.interceptors.AdminAuthAnnotation;
import com.jieweifu.models.ResultModel;
import com.jieweifu.models.admin.RoleAuthorityModel;
import com.jieweifu.models.admin.RoleModel;
import com.jieweifu.services.admin.RoleAuthorityService;
import com.jieweifu.services.admin.RoleService;
import com.jieweifu.services.admin.RoleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@SuppressWarnings("unused")
@Controller("SystemRole")
@RequestMapping("sys/role")
@AdminAuthAnnotation
public class RoleController {

    private RoleService roleService;
    private RoleUserService roleUserService;
    private RoleAuthorityService roleAuthorityService;

    @Autowired
    public RoleController(RoleService roleService
            , RoleUserService roleUserService
            , RoleAuthorityService roleAuthorityService){
        this.roleService = roleService;
        this.roleUserService = roleUserService;
        this.roleAuthorityService = roleAuthorityService;
    }

    /**
     * 查找全部角色
     * @return
     */
    @GetMapping("getAllRoles")
    @ResponseBody
    @AdminAuthAnnotation(check = false)
    public ResultModel getAllRoles(){
        return new ResultModel().setData(getRoleModelTree(1));
    }

    /**
     * 角色信息Tree生成方法
     * @param cid
     * @return
     */
    public RoleModel getRoleModelTree(int cid){
        RoleModel pRoleModel = roleService.getRoleById(cid);
        List<RoleModel> cRoleModelList = roleService.getRoleByParentId(pRoleModel.getId());
       for(RoleModel roleModel:cRoleModelList){
           RoleModel m = getRoleModelTree(roleModel.getId());
           pRoleModel.getChildren().add(m);
       }
        return pRoleModel;
    }

    /**
     * 根据id查找角色
     * @param id
     * @return
     */
    @GetMapping("getRoleById")
    @ResponseBody
    @AdminAuthAnnotation(check = false)
    public ResultModel getRoleById(int id){
        if(id<1)
            throw new RuntimeException("id不合法");
        RoleModel roleModel = roleService.getRoleById(id);
        if(roleModel==null)
            throw new RuntimeException("查找的数据不存在");
        return new ResultModel().setData(roleModel);
    }

    /**
     * 更新角色
     * @param roleModel
     * @return
     */
    @PutMapping("updateRole")
    @ResponseBody
    @AdminAuthAnnotation(check = false)
    public ResultModel updateRole(RoleModel roleModel){
        int row = 0;
       if(roleService.getRoleById(roleModel.getId())!=null
                &&roleModel.getRoleName()!=null
                &&roleModel.getRoleCode()!=null){
            row = roleService.updateRole(roleModel);
        }
        return new ResultModel().setMessage(row!=0?"更新成功":"更新失败");
    }

    /**
     * 删除角色,超级管理员不允许删除,
     * 分类不存在删除失败,
     * 分类下有子分类不允许删除,
     * 角色下有用户在使用,不允许删除
     * @param id
     * @return
     */
    @DeleteMapping("deleteRole")
    @ResponseBody
    @AdminAuthAnnotation(check = false)
    public ResultModel deleteRole(int id){
        if(id==1)
            throw new RuntimeException("超级管理员不允许删除");
        if(roleService.getRoleById(id)==null)
            throw new RuntimeException("分类不存在");
        //分类下不为空则不允许删除
        if(!roleService.getRoleByParentId(id).isEmpty())
            return new ResultModel().setMessage("分类下不为空,不允许删除");
        //角色下用户不为空不允许删除
        if(roleUserService.getRoleUserByRoleId(id)!=null)
            return new ResultModel().setMessage("角色下用户不为空,不允许删除");
        //删除菜单
        roleService.deleteRole(id);
        //删除权限
        roleAuthorityService.deleteRoleAuthority(roleService.getRoleById(id).getId());
        return new ResultModel().setMessage("删除角色成功");

    }

    /**
     * 添加角色
     * @param
     * @return
     */
    @PostMapping("addRole")
    @ResponseBody
    @AdminAuthAnnotation(check = false)
    public ResultModel addRole(RoleModel roleModel){
        boolean flag = true;
        if(roleService.getRoleById(roleModel.getId())==null
                &&roleModel.getRoleName()!=null
                &&roleModel.getRoleCode()!=null){
            roleService.addRole(roleModel);
        }
        else {
            flag=false;
        }
        return new ResultModel().setMessage(flag?"新增成功":"新增失败");
    }

    /**
     * 显示该角色所有权限
     * @param roleId
     * @return
     */
    @GetMapping("getAuthority")
    @ResponseBody
    @AdminAuthAnnotation(check = false)
    public ResultModel getAuthority(int roleId){
        if(roleId==0)
            throw new RuntimeException("该角色不存在");
        List<RoleAuthorityModel> roleAuthorityModelList
                = roleAuthorityService.getRoleAuthority(roleId);
        return new ResultModel().setData(roleAuthorityModelList);
    }

    /**
     * 添加权限
     * @param id,resourceIds
     * @return
     */
    @PostMapping("addAuthority")
    @ResponseBody
    @AdminAuthAnnotation(check = false)
    public ResultModel addAuthority(int id,String resourceIds){
        boolean count = false;
        if(resourceIds==null)
            throw new RuntimeException("权限不能为空");
        String[] resourceId = resourceIds.split(",");
        for(String rId:resourceId){
            roleAuthorityService.addRoleAuthority(id,Integer.parseInt(rId));
            count = true;
        }
        return new ResultModel().setMessage(count?"修改权限成功":"修改权限失败");
    }

    /**
     * 修改权限
     * @param id
     * @param resourceIds
     * @return
     */
    @PutMapping("updateAuthority")
    @ResponseBody
    @AdminAuthAnnotation(check = false)
    public ResultModel updateAuthority(int id,String resourceIds){
        roleAuthorityService.deleteRoleAuthority(id);
        return addAuthority(id,resourceIds);
    }
}
