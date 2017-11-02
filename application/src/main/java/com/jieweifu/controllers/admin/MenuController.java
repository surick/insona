package com.jieweifu.controllers.admin;

import com.jieweifu.interceptors.AdminAuthAnnotation;
import com.jieweifu.models.Result;
import com.jieweifu.models.admin.Menu;
import com.jieweifu.services.admin.MenuService;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@Controller("SystemMenu")
@RequestMapping("sys/menu")
@AdminAuthAnnotation
public class MenuController {
    private MenuService menuService;

    @Autowired
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    /**
     * 查询全部menu生成tree
     */
    @GetMapping("listAllMenus")
    public Result listAllMenus() {
        List<Menu> pMenuList = menuService.getMenuByParentId(-1);
        List<Menu> menuList = new ArrayList<>();
        pMenuList.forEach(
                menu -> {
                    Menu role1 = menuService.getMenuById(menu.getId());
                    menuList.add(role1);
                }
        );
        menuList.remove(0);
        return new Result().setData(menuList);
    }

    /**
     * 递归生成Tree
     */
    private Menu MenuTree(int cid) {
        Menu pMenu = menuService.getMenuById(cid);
        List<Menu> cMenuList = menuService.getMenuByParentId(pMenu.getId());
        for (Menu Menu : cMenuList) {
            Menu m = MenuTree(Menu.getId());
            pMenu.getChildren().add(m);
        }
        return pMenu;
    }


    /**
     * 根据id查找menu
     */
    @GetMapping("getMenuById/{id}")
    public Result getMenuById(@PathVariable("id") int id) {
        if (id < 1)
            return new Result().setError("id不合法");
        Menu Menu = menuService.getMenuById(id);
        if (Menu == null)
            return new Result().setError("查找的数据不存在");
        return new Result().setData(Menu);
    }

    /**
     * 更新menu信息,
     */
    @PutMapping("updateMenu")
    public Result updateMenu(@RequestBody Menu Menu) {
        boolean flag = true;
        if (menuService.getMenuById(Menu.getId()) != null
                && Menu.getTitle() != null
                && Menu.getHref() != null
                && Menu.getIcon() != null) {
            menuService.updateMenu(Menu);
        } else {
            flag = false;
        }
        return new Result().setMessage(flag ? "更新成功" : "更新失败");
    }

    /**
     * 删除菜单
     */
    @DeleteMapping("removeMenu/{id}")
    public Result removeMenu(@PathVariable("id") int id) {
        if (id <= 1)
            return new Result().setError("非法id");
        Menu Menu = menuService.getMenuById(id);
        if (Menu == null)
            return new Result().setError("分类不存在");
        if (menuService.getMenuByParentId(Menu.getId()) != null)
            return new Result().setError("分类下不为空,不允许删除");
        menuService.deleteMenu(id);
        return new Result().setMessage("删除成功");
    }

    /**
     * 添加菜单
     */
    @PostMapping("saveMenu")
    public Result saveMenu(@RequestBody Menu Menu) {
        if (menuService.getMenuById(Menu.getId()) == null
                && Menu.getTitle() != null
                && Menu.getHref() != null) {
            return new Result().setMessage(menuService.addMenu(Menu) == 0 ? "新增失败" : "新增成功");
        } else {
            return new Result().setMessage("字段不能为空");
        }
    }
}
