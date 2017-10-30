package com.jieweifu.services.admin.impl;

import com.jieweifu.common.business.OperateHandler;
import com.jieweifu.common.dbservice.DB;
import com.jieweifu.models.admin.RoleAuthority;
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
    public RoleAuthorityServiceImpl(DB db, MenuService menuService) {
        this.db = db;
        this.menuService = menuService;
    }

    @Override
    public List<RoleAuthority> getRoleAuthority(int roleId) {
        return db.select()
                .from(RoleAuthority.class)
                .where("role_id = ?", roleId)
                .queryForList(RoleAuthority.class);
    }

    @Override
    public int addRoleAuthority(int roleId, int rId) {
        int rows = 0;
        String resourceType = menuService.getMenuById(rId).getTitle();
        RoleAuthority RoleAuthority = new RoleAuthority();
        RoleAuthority.setRoleId(roleId);
        RoleAuthority.setResourceId(rId);
        RoleAuthority.setResourceType(resourceType);
        rows = db.insert().save(RoleAuthority).execute();
        return rows;
    }

    @Override
    public int deleteRoleAuthority(int roleId) {
        return db.delete()
                .from(RoleAuthority.class)
                .where("role_id = ?", roleId)
                .execute();
    }

    @Override
    public RoleAuthority getRoleAuthorityById(int id) {
        return db.select()
                .columns("id", "resource_type")
                .from(RoleAuthority.class)
                .where("id = ?", id)
                .queryForEntity(RoleAuthority.class);
    }

    @Override
    public int updateRoleAuthority(RoleAuthority RoleAuthority) {
        OperateHandler.assignUpdateUser(RoleAuthority);
        return db.update().save(RoleAuthority.class).execute();
    }
}
