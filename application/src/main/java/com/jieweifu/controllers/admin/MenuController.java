package com.jieweifu.controllers.admin;

import com.jieweifu.interceptors.AdminAuthAnnotation;
import com.jieweifu.models.Result;
import com.jieweifu.models.admin.Menu;
import com.jieweifu.services.admin.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping("getAllMenus")
    public Result getAllMenus() {
        return new Result().setData(MenuTree(1));
    }

    /**
     * 递归生成Tree
     */
    private Menu MenuTree(int cid) {
        //根据id获取节点对象
        Menu pMenu = menuService.getMenuById(cid);
        //查询id下的所有子节点
        List<Menu> cMenuList = menuService.getMenuByParentId(pMenu.getId());
        //遍历子节点
        for (Menu Menu : cMenuList) {
            Menu m = MenuTree(Menu.getId());
            //添加一个集合,将子级放入父级集合
            pMenu.getChildren().add(m);
        }
        return pMenu;
    }


    /**
     * 根据id查找menu
     */
    @GetMapping("getMenuById")
    public Result getMenuById(int id) {
        if (id < 1)
            throw new RuntimeException("id不合法");
        Menu Menu = menuService.getMenuById(id);
        if (Menu == null)
            throw new RuntimeException("查找的数据不存在");
        return new Result().setData(Menu);
    }

    /**
     * 更新menu信息,
     */
    @PutMapping("updateMenu")
    public Result updateMenu(Menu Menu) {
        boolean flag = true;
        if (menuService.getMenuById(Menu.getId()) != null //判断分类是否存在
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
    @DeleteMapping("deleteMenu")
    public Result deleteMenu(int id) {
        if (id == 1)
            throw new RuntimeException("系统管理不允许删除");
        Menu Menu = menuService.getMenuById(id);
        if (Menu == null)
            throw new RuntimeException("分类不存在");
        //分类下不为空则不允许删除
        if (menuService.getMenuByParentId(Menu.getId()) != null)
            return new Result().setMessage("分类下不为空,不允许删除");
        //删除菜单
        menuService.deleteMenu(id);
        return new Result().setMessage("删除成功");
    }

    /**
     * 添加菜单
     */
    @GetMapping("addMenu")
    public Result addMenu(Menu Menu) {
        //判断字段是否为空
        if (menuService.getMenuById(Menu.getId()) == null
                && Menu.getTitle() != null
                && Menu.getHref() != null) {
            return new Result().setMessage(menuService.addMenu(Menu) == 0 ? "新增失败" : "新增成功");
        } else {
            //为空保存失败
            return new Result().setMessage("字段不能为空");
        }
    }
}
