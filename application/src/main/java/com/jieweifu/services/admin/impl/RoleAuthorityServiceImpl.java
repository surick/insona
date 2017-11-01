package com.jieweifu.services.admin.impl;

import com.jieweifu.common.business.OperateHandler;
import com.jieweifu.common.dbservice.DB;
import com.jieweifu.models.MenuElements;
import com.jieweifu.models.admin.Element;
import com.jieweifu.models.admin.Menu;
import com.jieweifu.models.admin.RoleAuthority;
import com.jieweifu.services.admin.RoleAuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleAuthorityServiceImpl implements RoleAuthorityService {


    private DB db;

    @Autowired
    public RoleAuthorityServiceImpl(DB db) {
        this.db = db;
    }


    @Override
    public int addRoleAuthority(RoleAuthority roleAuthority) {
        int rows = 0;
        rows = db.insert().save(roleAuthority).execute();
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
    public List<RoleAuthority> getRoleAuthorityById(int roleId) {
        return db.select()
                .from(RoleAuthority.class)
                .where("role_id = ?", roleId)
                .queryForList(RoleAuthority.class);
    }


    @Override
    public List<Integer> getResourceId(int id) {
        return db.select()
                .columns("resource_id")
                .from(RoleAuthority.class)
                .where("id = ?", id)
                .queryForList(Integer.class);
    }


    @Override
    public int updateRoleAuthority(RoleAuthority RoleAuthority) {
        OperateHandler.assignUpdateUser(RoleAuthority);
        return db.update()
                .save(RoleAuthority.class)
                .execute();
    }

    @Override
    public List<String> getResourceType(int resourceId) {
        return db.select()
                .columns("resource_type")
                .from(RoleAuthority.class)
                .where("resource_id = ?", resourceId)
                .queryForList(String.class);
    }

    @Override
    public MenuElements getMenuElements(int roleId) {
        List<Menu> menus = getAllMenus();
        List<Element> elements = getAllElements();
        List<Menu> _menus = new ArrayList<>();
        List<Element> _elements = new ArrayList<>();
        List<RoleAuthority> roleAuthorities = getRoleAuthorityById(roleId);
        menus.forEach(menu -> {
            if (roleAuthorities.stream().filter(p -> p.getResourceId() == menu.getId()
                    && p.getResourceType().equalsIgnoreCase("MENU")).count() > 0) {
                _menus.add(menu);
            }
        });
        elements.forEach(element -> {
            if (roleAuthorities.stream().filter(p -> p.getResourceId() == element.getId()
                    && p.getResourceType().equalsIgnoreCase("ELEMENT")).count() > 0) {
                _elements.add(element);
            }
        });
        menus = _menus;
        elements = _elements;
        return new MenuElements(menus, elements);
    }

    private List<Menu> getAllMenus() {
        return db.select()
                .columns("id, code, title, parent_id, href, icon, type, order_num, description, path, enabled")
                .from(Menu.class)
                .queryForList(Menu.class);
    }

    private List<Element> getAllElements() {
        return db.select()
                .columns("id, code, type, element_name, uri, menu_id, parent_id, path, method, description")
                .from(Element.class)
                .queryForList(Element.class);
    }
}
