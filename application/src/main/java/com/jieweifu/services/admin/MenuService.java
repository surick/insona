package com.jieweifu.services.admin;

import com.jieweifu.models.admin.Menu;

import java.util.List;

@SuppressWarnings("unused")
public interface MenuService {
    List<Menu> getAllMenus();
    Menu getMenuById(int id);
    void updateMenu(Menu menu);
    List<Menu> getMenuByParentId(int ParentId);
    void deleteMenu(int id);
    int addMenu(Menu menu);
}
