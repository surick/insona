package com.jieweifu.services.admin;

import com.jieweifu.models.admin.RoleAuthorityModel;

import java.util.List;

public interface RoleAuthorityService {
    List<RoleAuthorityModel> getRoleAuthority(int pageIndex, int pageSize);
    int addRoleAuthority(RoleAuthorityModel roleAuthorityModel);
    int deleteRoleAuthority(int roleId);
    RoleAuthorityModel getRoleAuthorityById(int id);
    int updateRoleAuthority(RoleAuthorityModel roleAuthorityModel);
}
