package com.jieweifu.services.admin.impl;

import com.jieweifu.common.dbservice.DB;
import com.jieweifu.models.admin.MenuModel;
import com.jieweifu.services.admin.MenuService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuServiceImpl implements MenuService {

    private DB db;

    public MenuServiceImpl(DB db) {
        this.db = db;
    }

    @Override
    public List<MenuModel> getAllMenus() {
        return db.select()
                .from(MenuModel.class)
                .queryForList(MenuModel.class);
    }

    @Override
    public MenuModel getMenuById(int id) {
        return db.select()
                .from(MenuModel.class)
                .where("id = ?", id)
                .queryForEntity(MenuModel.class);
    }

    @Override
    public void updateMenu(MenuModel menuModel) {
        db.update()
                .save(menuModel)
                .execute();
    }

    @Override
    public void deleteMenu(int id) {
        db.delete()
                .from(MenuModel.class)
                .where("id = ?", id)
                .execute();
    }

    @Override
    public int addMenu(MenuModel menuModel) {
        return db.insert().save(menuModel).execute();
    }
}
