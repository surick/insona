package com.jieweifu.services.admin;

import com.jieweifu.models.admin.MenuModel;

import java.util.List;

@SuppressWarnings("unused")
public interface MenuService {
    List<MenuModel> getAllMenus();
    MenuModel getMenuById(int id);
    List<MenuModel> getMenuByParentId(int ParentId);
    void updateMenu(MenuModel menuModel);
    void deleteMenu(int id);
    int addMenu(MenuModel menuModel);
}
