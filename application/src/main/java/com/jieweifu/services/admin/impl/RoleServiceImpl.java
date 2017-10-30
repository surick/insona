package com.jieweifu.services.admin.impl;

import com.jieweifu.common.business.OperateHandler;
import com.jieweifu.common.dbservice.DB;
import com.jieweifu.models.admin.RoleModel;
import com.jieweifu.services.admin.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {
    private DB db;

    @Autowired
    public RoleServiceImpl(DB db){
        this.db = db;
    }

    @Override
    public List<RoleModel> getAllRoles() {
        return db.select()
                .from(RoleModel.class)
                .queryForList(RoleModel.class);
    }

    @Override
    public RoleModel getRoleById(int id) {
        return db.select()
                .from(RoleModel.class)
                .where("id=?",id)
                .queryForEntity(RoleModel.class);
    }

    @Override
    public List<RoleModel> getRoleByParentId(int ParentId) {
        return db.select()
                .from(RoleModel.class)
                .where("parent_id=?",ParentId)
                .queryForList(RoleModel.class);
    }

    @Override
    public int updateRole(RoleModel roleModel) {
        OperateHandler.assignUpdateUser(roleModel);
        return db.update().save(roleModel).execute();
    }

    @Override
    public void deleteRole(int id) {
        db.delete()
                .from(RoleModel.class)
                .where("id = ?",id)
                .execute();
    }

    @Override
    public int addRole(RoleModel roleModel) {
        OperateHandler.assignCreateUser(roleModel);
        return db.insert().save(roleModel).execute();
    }
}
