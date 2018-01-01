package com.jieweifu.services.admin.impl;

import com.jieweifu.common.business.OperateHandler;
import com.jieweifu.common.dbservice.DB;
import com.jieweifu.models.admin.Role;
import com.jieweifu.models.admin.User;
import com.jieweifu.services.admin.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private DB db;

    @Autowired
    public RoleServiceImpl(DB db) {
        this.db = db;
    }

    @Override
    public List<Role> getAllRoles() {
        return db.select()
                .from(Role.class)
                .queryForList(Role.class);
    }

    @Override
    public Role getRoleById(int id) {
        return db.select()
                .from(Role.class)
                .where("id = ?", id)
                .queryForEntity(Role.class);
    }

    @Override
    public List<Role> getRoleByParentId(int ParentId) {
        return db.select()
                .from(Role.class)
                .where("parent_id=?", ParentId)
                .queryForList(Role.class);
    }

    @Override
    public int updateRole(Role Role) {
        OperateHandler.assignUpdateUser(Role);
        return db.update().save(Role).execute();
    }

    @Override
    public void deleteRole(int id) {
        db.delete()
                .from(Role.class)
                .where("id = ?", id)
                .execute();
    }

    @Override
    public int addRole(Role role) {
        OperateHandler.assignCreateUser(role);
        return db.insert().save(role).execute();
    }

    @Override
    public Role getRoleByName(String roleName) {
        return db.select()
                .from(Role.class)
                .where("role_name = ?", roleName)
                .queryForEntity(Role.class);
    }

    @Override
    public List<User> getProducerUser(String roleName) {
       return db.select().from("base_user AS A","base_role AS B","base_role_user AS C")
                .where("A.id=C.user_id AND B.id = C.role_id AND B.role_name = ?",roleName).queryForList(User.class);
    }

}
