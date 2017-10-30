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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
    @PostMapping("getAllRoles")
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
    @PostMapping("getRoleById")
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
    @PostMapping("putRole")
    @ResponseBody
    @AdminAuthAnnotation(check = false)
    public ResultModel putRole(RoleModel roleModel){
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
    @PostMapping("deleteRole")
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
    @PostMapping("postRole")
    @ResponseBody
    @AdminAuthAnnotation(check = false)
    public ResultModel postRole(RoleModel roleModel){
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
     * 添加权限
     * @param roleAuthorityModel
     * @return
     */
    @PostMapping("postAuthority")
    @ResponseBody
    @AdminAuthAnnotation(check = false)
    public ResultModel postAuthority(RoleAuthorityModel roleAuthorityModel){
        int count = 0;
        if(roleAuthorityModel.getRoleId()!=0
                &&roleAuthorityModel.getResourceId()!=0
                &&roleAuthorityModel.getResourceType()!=null){
        }
        count = roleAuthorityService.addRoleAuthority(roleAuthorityModel);
        return new ResultModel().setMessage(count!=0?"添加权限成功":"添加权限失败");
    }

}
