package com.jieweifu.services.admin.impl;

import com.jieweifu.common.dbservice.DB;
import com.jieweifu.models.admin.*;
import com.jieweifu.services.admin.UserService;
import com.jieweifu.vo.admin.MenuElement;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private DB db;

    public UserServiceImpl(DB db) {
        this.db = db;
    }

    @Override
    public List<UserModel> getAllUsers() {
        return db.select()
                .from(UserModel.class)
                .queryForList(UserModel.class);
    }

    @Override
    public int addUser(UserModel userModel) {
        return db.insert()
                .save(userModel)
                .execute();
    }

    @Override
    public UserModel doUserLogin(String userName, String password) {
        return db.select()
                .columns("id, head_img_url, name")
                .from(UserModel.class)
                .where("user_name = ?", userName)
                .where("password = MD5(CONCAT(salt, ?))", password)
                .queryForEntity(UserModel.class);
    }

    @Override
    public UserModel doUserLogin(int userId, String password) {
        return db.select()
                .columns("id, head_img_url, name")
                .from(UserModel.class)
                .where("id = ?", userId)
                .where("password = MD5(CONCAT(salt, ?))", password)
                .queryForEntity(UserModel.class);
    }

    @Override
    public UserModel getUserById(int id) {
        return db.select()
                .from(UserModel.class)
                .where("id = ?", id)
                .queryForEntity(UserModel.class);
    }

    @Override
    public void updateUserHeadImg(int userId, String headImgUrl) {
        UserModel userModel = new UserModel();
        userModel.setId(userId);
        userModel.setHeadImgUrl(headImgUrl);
        updateUser(userModel);
    }

    @Override
    public void updateUser(UserModel userModel) {
        db.update()
                .save(userModel)
                .execute();
    }

    @Override
    public void updateUserPassword(int userId, String password) {
        db.update()
                .set("password = MD5(CONCAT(salt, ?))", password)
                .where("id = ?", userId)
                .execute();
    }

    @Override
    public boolean getIsAdmin(int userId) {
        return db.select()
                .columns("1")
                .from(RoleUserModel.class, "A")
                .leftOuterJoin(RoleModel.class, "B", "A.role_id = B.id")
                .where("A.user_id = ? AND B.role_code = 'admin'", userId)
                .limit(0, 1)
                .total() > 0;
    }

    @Override
    public List<MenuElement> getMenuElements(int userId, boolean isAdmin) {
        List<MenuModel> menuModels = getAllMenus();
        List<ElementModel> elementModels = getAllElements();

        if (!isAdmin) {
            List<MenuModel> _menuModels = new ArrayList<>();
            List<ElementModel> _elementModels = new ArrayList<>();

            List<RoleAuthorityModel> roleAuthorityModels = getRoleAuthorization(userId);
            menuModels.forEach(menuModel -> {
                if (roleAuthorityModels.stream().filter(p -> p.getResourceId() == menuModel.getId()
                        && p.getResourceType().equalsIgnoreCase("MENU")).count() > 0) {
                    _menuModels.add(menuModel);
                }
            });
            elementModels.forEach(elementModel -> {
                if (roleAuthorityModels.stream().filter(p -> p.getResourceId() == elementModel.getId()
                        && p.getResourceType().equalsIgnoreCase("ELEMENT")).count() > 0) {
                    _elementModels.add(elementModel);
                }
            });
            menuModels = _menuModels;
            elementModels = _elementModels;
        }

        return generateMenuElements(menuModels, elementModels);
    }

    @Override
    public List<MenuElement> getAllMenuElements() {
        return getMenuElements(-1, true);
    }

    @Override
    public List<ElementModel> getAllAuthElements(int userId, boolean isAdmin) {
        List<ElementModel> elementModels = getAllElements();

        if (!isAdmin) {
            List<ElementModel> _elementModels = new ArrayList<>();
            List<RoleAuthorityModel> roleAuthorityModels = getRoleAuthorization(userId);
            elementModels.forEach(elementModel -> {
                if (roleAuthorityModels.stream().filter(p -> p.getResourceId() == elementModel.getId()
                        && p.getResourceType().equalsIgnoreCase("ELEMENT")).count() > 0) {
                    _elementModels.add(elementModel);
                }
            });
            elementModels = _elementModels;
        }

        return elementModels;
    }

    private List<MenuElement> generateMenuElements(List<MenuModel> menuModels, List<ElementModel> elementModels) {
        List<MenuElement> menuElements = new ArrayList<>();
        findNext(-1, menuModels, menuElements, elementModels);
        return menuElements;
    }

    private void findNext(int parentId, List<MenuModel> menuModels, List<MenuElement> menuElements, List<ElementModel> elementModels) {
        List<MenuModel> _menuModels = menuModels.stream().filter(p -> p.getParentId() == parentId).collect(Collectors.toList());
        _menuModels.forEach(_menuModel -> {
            MenuElement menuElement = new MenuElement();
            menuElement.setMenu(_menuModel);
            elementModels.stream().filter(elementModel -> elementModel.getMenuId() == _menuModel.getId()).forEach(menuElement::setElement);
            findNext(_menuModel.getId(), menuModels, menuElement.getChildren(), elementModels);
            menuElements.add(menuElement);
        });
    }

    private List<RoleAuthorityModel> getRoleAuthorization(int userId) {
        return db.select()
                .columns("DISTINCT C.resource_id, C.resource_type")
                .from(RoleUserModel.class, "A")
                .leftOuterJoin(RoleAuthorityModel.class, "B", "B.role_id = A.role_id")
                .where("A.user_id = ?", userId)
                .queryForList(RoleAuthorityModel.class);
    }

    private List<MenuModel> getAllMenus() {
        return db.select()
                .columns("id, code, title, parent_id, href, icon, type, order_num, description, path, enabled")
                .from(MenuModel.class)
                .queryForList(MenuModel.class);
    }

    private List<ElementModel> getAllElements() {
        return db.select()
                .columns("id, code, type, element_name, uri, menu_id, parent_id, path, method, description")
                .from(ElementModel.class)
                .queryForList(ElementModel.class);
    }
}
