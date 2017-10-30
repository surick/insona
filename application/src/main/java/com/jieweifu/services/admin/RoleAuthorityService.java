package com.jieweifu.services.admin;

import com.jieweifu.models.admin.RoleAuthority;

import java.util.List;

public interface RoleAuthorityService {
    List<RoleAuthority> getRoleAuthority(int roleId);
    int addRoleAuthority(int roleId,int resourceIds);
    int deleteRoleAuthority(int roleId);
    RoleAuthority getRoleAuthorityById(int id);
    int updateRoleAuthority(RoleAuthority RoleAuthority);
}
