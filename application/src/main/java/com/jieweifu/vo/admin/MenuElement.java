package com.jieweifu.vo.admin;

import com.jieweifu.models.admin.Element;
import com.jieweifu.models.admin.Menu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class MenuElement implements Serializable{
    static final long serialVersionUID = 1L;

    private Menu menu;
    private List<MenuElement> children = new ArrayList<>();
    private List<Element> elements = new ArrayList<>();

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu){
        this.menu = menu;
    }

    public void setElement(Element element){
        this.elements.add(element);
    }

    public List<Element> getElements() {
        return elements;
    }

    public void setChild(MenuElement menu){
        this.children.add(menu);
    }

    public List<MenuElement> getChildren() {
        return children;
    }
}
