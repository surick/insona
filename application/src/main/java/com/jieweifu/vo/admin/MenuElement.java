package com.jieweifu.vo.admin;

import com.jieweifu.models.admin.ElementModel;
import com.jieweifu.models.admin.MenuModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class MenuElement implements Serializable{
    static final long serialVersionUID = 1L;

    private MenuModel menu;
    private List<MenuElement> children = new ArrayList<>();
    private List<ElementModel> elements = new ArrayList<>();

    public MenuModel getMenu() {
        return menu;
    }

    public void setMenu(MenuModel menu){
        this.menu = menu;
    }

    public void setElement(ElementModel element){
        this.elements.add(element);
    }

    public List<ElementModel> getElements() {
        return elements;
    }

    public void setChild(MenuElement menu){
        this.children.add(menu);
    }

    public List<MenuElement> getChildren() {
        return children;
    }
}
