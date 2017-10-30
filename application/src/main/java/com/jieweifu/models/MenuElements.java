package com.jieweifu.models;

import com.jieweifu.models.admin.ElementModel;
import com.jieweifu.models.admin.MenuModel;

import java.util.List;

public class MenuElementsModel {
    private List<MenuModel> menus;
    private List<ElementModel> elements;

    public MenuElementsModel(List<MenuModel> menuModels, List<ElementModel> elementModels) {
        this.menus = menuModels;
        this.elements = elementModels;
    }

    public List<MenuModel> getMenus() {
        return menus;
    }

    public void setMenus(List<MenuModel> menus) {
        this.menus = menus;
    }

    public List<ElementModel> getElements() {
        return elements;
    }

    public void setElements(List<ElementModel> elements) {
        this.elements = elements;
    }
}
