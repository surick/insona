package com.jieweifu.services.admin;

import com.jieweifu.models.admin.RoleModel;

import java.util.List;

public interface RoleService {
    List<RoleModel> getAllRoles();
    RoleModel getRoleById(int id);
    List<RoleModel> getRoleByParentId(int ParentId);
    int updateRole(RoleModel roleModel);
    void deleteRole(int id);
    int addRole(RoleModel roleModel);
}
