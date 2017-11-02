package com.jieweifu.services.admin.impl;

import com.jieweifu.common.business.OperateHandler;
import com.jieweifu.common.dbservice.DB;
import com.jieweifu.models.MenuElements;
import com.jieweifu.models.admin.*;
import com.jieweifu.services.admin.UserService;
import com.jieweifu.vo.admin.MenuElement;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private DB db;

    public UserServiceImpl(DB db) {
        this.db = db;
    }

    @Override
    public List<User> getAllUsers() {
        return db.select()
                .from(User.class)
                .queryForList(User.class);
    }

    @Override
    public int addUser(User user) {
        OperateHandler.assignCreateUser(user);
        String salt = UUID.randomUUID().toString().replace("-", "");
        user.setPassword(DigestUtils.md5Hex(salt + user.getPassword()));
        return db.insert()
                .save(user)
                .set("salt", salt)
                .execute();
    }

    @Override
    public User doUserLogin(String userName, String password) {
        return db.select()
                .columns("id, head_img_url, name")
                .from(User.class)
                .where("user_name = ?", userName)
                .where("password = MD5(CONCAT(salt, ?))", password)
                .queryForEntity(User.class);
    }

    @Override
    public boolean doUserLogin(int userId, String password) {
        return db.select()
                .from(User.class)
                .where("id = ?", userId)
                .where("password = MD5(CONCAT(salt, ?))", password)
                .total() > 0;
    }

    @Override
    public User getUserById(int id) {
        return db.select()
                .from(User.class)
                .where("id = ?", id)
                .queryForEntity(User.class);
    }

    @Override
    public void updateUserHeadImg(int userId, String headImgUrl) {
        User user = new User();
        OperateHandler.assignUpdateUser(user);
        user.setId(userId);
        user.setHeadImgUrl(headImgUrl);
        updateUser(user);
    }

    @Override
    public void updateUser(User user) {
        OperateHandler.assignUpdateUser(user);
        db.update()
                .save(user)
                .execute();
    }

    @Override
    public void updateUserPassword(int userId, String password) {
        db.update()
                .table(User.class)
                .set("password = MD5(CONCAT(salt, ?))", password)
                .where("id = ?", userId)
                .execute();
    }

    @Override
    public boolean getIsAdmin(int userId) {
        return db.select()
                .columns("1")
                .from(RoleUser.class, "A")
                .leftOuterJoin(Role.class, "B", "A.role_id = B.id")
                .where("A.user_id = ? AND B.role_code = 'admin'", userId)
                .limit(0, 1)
                .total() > 0;
    }

    public MenuElements getMenuElements(int userId, boolean isAdmin) {
        List<Menu> menus = getAllMenus();
        List<Element> elements = getAllElements();

        if (!isAdmin) {
            List<Menu> _menus = new ArrayList<>();
            List<Element> _elements = new ArrayList<>();

            List<RoleAuthority> roleAuthorities = getRoleAuthorization(userId);
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
        }

        return new MenuElements(menus, elements);
    }

    @Override
    public MenuElements getAllMenuElements() {
        return getMenuElements(-1, true);
    }

    @Override
    public List<Element> getAllAuthElements(int userId, boolean isAdmin) {
        List<Element> elements = getAllElements();

        if (!isAdmin) {
            List<Element> _elements = new ArrayList<>();
            List<RoleAuthority> roleAuthorities = getRoleAuthorization(userId);
            elements.forEach(element -> {
                if (roleAuthorities.stream().filter(p -> p.getResourceId() == element.getId()
                        && p.getResourceType().equalsIgnoreCase("ELEMENT")).count() > 0) {
                    _elements.add(element);
                }
            });
            elements = _elements;
        }

        return elements;
    }

    @Override
    public List<User> getUsersByPage(int pageIndex, int pageSize) {
        return db.select()
                .from(User.class)
                .where("isDelete = ? AND id != 1", 0)
                .limit(pageIndex, pageSize)
                .queryForList(User.class);
    }

    @Override
    public User getUserByUserName(String userName) {
        return db.select()
                .from(User.class)
                .where("user_name = ?", userName)
                .queryForEntity(User.class);
    }

    @Override
    public void deleteUser(int id) {
        db.delete()
                .from(User.class)
                .where("id = ?", id)
                .execute();
    }

    @Override
    public int getUserTotal() {
        return db.select()
                .from(User.class)
                .where("isDelete = ? AND id != 1", 0)
                .total();
    }

    @Override
    public void isDelete(int id) {
        //db.getJdbcTemplate().execute("ALTER TABLE base_user ADD isDelete int");
        db.update().table(User.class).set("isDelete = ?", 1).where("id = ?", id).execute();
    }

    private List<MenuElement> generateMenuElements(List<Menu> menus, List<Element> elements) {
        List<MenuElement> menuElements = new ArrayList<>();
        findNext(-1, menus, menuElements, elements);
        return menuElements;
    }

    private void findNext(int parentId, List<Menu> menus, List<MenuElement> menuElements, List<Element> elements) {
        List<Menu> _menus = menus.stream().filter(p -> p.getParentId() == parentId).collect(Collectors.toList());
        _menus.forEach(_menu -> {
            MenuElement menuElement = new MenuElement();
            menuElement.setMenu(_menu);
            elements.stream().filter(element -> element.getMenuId() == _menu.getId()).forEach(menuElement::setElement);
            findNext(_menu.getId(), menus, menuElement.getChildren(), elements);
            menuElements.add(menuElement);
        });
    }

    private List<RoleAuthority> getRoleAuthorization(int userId) {
        return db.select()
                .columns("DISTINCT C.resource_id, C.resource_type")
                .from(RoleUser.class, "A")
                .leftOuterJoin(RoleAuthority.class, "B", "B.role_id = A.role_id")
                .where("A.user_id = ?", userId)
                .queryForList(RoleAuthority.class);
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
