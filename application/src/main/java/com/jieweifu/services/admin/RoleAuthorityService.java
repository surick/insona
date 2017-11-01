package com.jieweifu.services.admin;

import com.jieweifu.models.MenuElements;
import com.jieweifu.models.admin.RoleAuthority;

import java.util.List;

public interface RoleAuthorityService {
    List<Integer> getResourceId(int roleId);

    int addRoleAuthority(RoleAuthority roleAuthority);

    int deleteRoleAuthority(int roleId);

    List<RoleAuthority> getRoleAuthorityById(int roleId);

    int updateRoleAuthority(RoleAuthority RoleAuthority);

    List<String> getResourceType(int resourceId);

    MenuElements getMenuElements(int userId);
}
