package com.jieweifu.services.admin;

import com.jieweifu.models.admin.Role;
import com.jieweifu.models.admin.User;

import java.util.List;

public interface RoleService {
    List<Role> getAllRoles();

    Role getRoleById(int id);

    List<Role> getRoleByParentId(int ParentId);

    int updateRole(Role Role);

    void deleteRole(int id);

    int addRole(Role Role);

    Role getRoleByName(String roleName);

    List<User> getProducerUser();

}
