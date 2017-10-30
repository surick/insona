package com.jieweifu.services.admin.impl;

import com.jieweifu.common.dbservice.DB;
import com.jieweifu.models.admin.RoleUser;
import com.jieweifu.services.admin.RoleUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleUserServiceImpl implements RoleUserService {

    private DB db;

    @Autowired
    public RoleUserServiceImpl(DB db) {
        this.db = db;
    }

    @Override
    public RoleUser getRoleUserByRoleId(int roleId) {
        return db.select()
                .from(RoleUser.class)
                .where("role_id = ?",roleId)
                .queryForEntity(RoleUser.class);
    }
}