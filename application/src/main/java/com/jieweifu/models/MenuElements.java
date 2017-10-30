package com.jieweifu.models;

import com.jieweifu.models.admin.Element;
import com.jieweifu.models.admin.Menu;

import java.util.List;

public class MenuElements {
    private List<Menu> menus;
    private List<Element> elements;

    public MenuElements(List<Menu> menus, List<Element> elements) {
        this.menus = menus;
        this.elements = elements;
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }

    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }
}
