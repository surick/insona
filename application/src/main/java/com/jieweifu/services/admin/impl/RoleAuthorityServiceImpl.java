package com.jieweifu.services.admin.impl;

import com.jieweifu.common.business.OperateHandler;
import com.jieweifu.common.dbservice.DB;
import com.jieweifu.models.admin.RoleAuthorityModel;
import com.jieweifu.services.admin.MenuService;
import com.jieweifu.services.admin.RoleAuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleAuthorityServiceImpl implements RoleAuthorityService {

    private DB db;
    private MenuService menuService;

    @Autowired
    public RoleAuthorityServiceImpl(DB db,MenuService menuService){
        this.db = db;this.menuService = menuService;
    }


    @Override
    public List<RoleAuthorityModel> getRoleAuthority(int roleId) {
        return db.select()
                .from(RoleAuthorityModel.class)
                .where("role_id = ?",roleId)
                .queryForList(RoleAuthorityModel.class);
    }

    @Override
    public int addRoleAuthority(int roleId,int rId) {
        int rows =0;
           String resourceType = menuService.getMenuById(rId).getTitle();
           RoleAuthorityModel roleAuthorityModel = new RoleAuthorityModel();
           roleAuthorityModel.setRoleId(roleId);
           roleAuthorityModel.setResourceId(rId);
           roleAuthorityModel.setResourceType(resourceType);
          rows = db.insert().save(roleAuthorityModel).execute();
        return rows;
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
