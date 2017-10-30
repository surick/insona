package com.jieweifu.services.admin;

import com.jieweifu.models.admin.RoleAuthorityModel;

import java.util.List;

public interface RoleAuthorityService {
    List<RoleAuthorityModel> getRoleAuthority(int roleId);
    int addRoleAuthority(int roleId,int resourceIds);
    int deleteRoleAuthority(int roleId);
    RoleAuthorityModel getRoleAuthorityById(int id);
    int updateRoleAuthority(RoleAuthorityModel roleAuthorityModel);
}
