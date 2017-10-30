package com.jieweifu.services.admin.impl;

import com.jieweifu.common.business.OperateHandler;
import com.jieweifu.common.dbservice.DB;
import com.jieweifu.models.admin.RoleAuthorityModel;
import com.jieweifu.services.admin.RoleAuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleAuthorityServiceImpl implements RoleAuthorityService {

    private DB db;

    @Autowired
    public RoleAuthorityServiceImpl(DB db){
        this.db = db;
    }


    @Override
    public List<RoleAuthorityModel> getRoleAuthority(int pageIndex, int pageSize) {
        return db.select()
                .from(RoleAuthorityModel.class)
                .limit(pageIndex,pageSize)
                .queryForList(RoleAuthorityModel.class);
    }

    @Override
    public int addRoleAuthority(RoleAuthorityModel roleAuthorityModel) {
        OperateHandler.assignCreateUser(roleAuthorityModel);
        return db.insert().save(roleAuthorityModel).execute();
    }

    @Override
    public int deleteRoleAuthority(int roleId) {
        return db.delete()
                .from(RoleAuthorityModel.class)
                .where("role_id = ?",roleId)
                .execute();

    }

    @Override
    public RoleAuthorityModel getRoleAuthorityById(int id) {
        return db.select()
                .columns("id","resource_type")
                .from(RoleAuthorityModel.class)
                .where("id = ?",id)
                .queryForEntity(RoleAuthorityModel.class);
    }

    @Override
    public int updateRoleAuthority(RoleAuthorityModel roleAuthorityModel) {
        OperateHandler.assignUpdateUser(roleAuthorityModel);
        return db.update().save(RoleAuthorityModel.class).execute();
    }
}
