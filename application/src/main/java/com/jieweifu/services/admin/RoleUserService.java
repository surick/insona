package com.jieweifu.services.admin;

import com.jieweifu.models.admin.RoleUser;


public interface RoleUserService {
    RoleUser getRoleUserByRoleId(int roleId);
    void addRoleUser(RoleUser roleUser);
    void deleteRoleUser(int userId);
}
