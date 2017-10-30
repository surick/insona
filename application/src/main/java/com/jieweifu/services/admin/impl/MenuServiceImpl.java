package com.jieweifu.services.admin.impl;

import com.jieweifu.common.business.OperateHandler;
import com.jieweifu.common.dbservice.DB;
import com.jieweifu.models.admin.Menu;
import com.jieweifu.services.admin.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {

    private DB db;

    @Autowired
    public MenuServiceImpl(DB db) {
        this.db = db;
    }

    @Override
    public List<Menu> getMenuByParentId(int parent_id) {
        return db.select()
                .from(Menu.class)
                .where("parent_id=?", parent_id)
                .queryForList(Menu.class);
    }

    @Override
    public List<Menu> getAllMenus() {
        return db.select()
                .from(Menu.class)
                .queryForList(Menu.class);
    }

    @Override
    public Menu getMenuById(int id) {
        return db.select()
                .from(Menu.class)
                .where("id = ?", id)
                .queryForEntity(Menu.class);
    }

    @Override
    public void updateMenu(Menu menu) {
        OperateHandler.assignUpdateUser(menu);
        db.update()
                .save(menu)
                .execute();
    }

    @Override
    public void deleteMenu(int id) {
        db.delete()
                .from(Menu.class)
                .where("id = ?", id)
                .execute();
    }

    @Override
    public int addMenu(Menu menu) {
        OperateHandler.assignCreateUser(menu);
        return db.insert()
                .save(menu)
                .execute();
    }
}
