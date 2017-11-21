package com.jieweifu.controllers.admin;

import com.jieweifu.common.utils.ErrorUtil;
import com.jieweifu.interceptors.AdminAuthAnnotation;
import com.jieweifu.models.Result;
import com.jieweifu.models.admin.Element;
import com.jieweifu.models.admin.Menu;
import com.jieweifu.services.admin.ElementService;
import com.jieweifu.services.admin.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
@RestController("SystemMenu")
@RequestMapping("sys/menu")
@AdminAuthAnnotation
public class MenuController {
    private MenuService menuService;
    private ElementService elementService;

    @Autowired
    public MenuController(MenuService menuService,
                          ElementService elementService) {
        this.menuService = menuService;
        this.elementService = elementService;
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
                    Menu menu1 = getMenuTree(menu.getId());
                    menuList.add(menu1);
                }
        );
        System.out.println(menuList);
        return new Result().setData(menuList);
    }

    /**
     * 递归生成Tree
     */
    private Menu getMenuTree(int cid) {
        Menu pMenu = menuService.getMenuById(cid);
        List<Menu> cMenuList = menuService.getMenuByParentId(pMenu.getId());
        for (Menu Menu : cMenuList) {
            Menu m = getMenuTree(Menu.getId());
            List<Element> elementList = elementService.listElement(m.getId());
            elementList.forEach(
                    element -> {
                        Menu menu1 = new Menu();
                        menu1.setId(element.getId());
                        menu1.setCode(element.getCode());
                        menu1.setType(element.getType());
                        menu1.setTitle(element.getElementName());
                        menu1.setParentId(element.getMenuId());
                        m.getChildren().add(menu1);
                    }
            );
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
    public Result updateMenu(@Valid @RequestBody Menu Menu, Errors errors) {
        if (errors.hasErrors()) {
            return new Result().setError(ErrorUtil.getErrors(errors));
        }
        boolean flag = true;
        if (menuService.getMenuById(Menu.getId()) != null) {
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
        if (menuService.getMenuById(id) == null)
            return new Result().setError("分类不存在");
        if (menuService.getMenuByParentId(id) != null)
            return new Result().setError("分类下不为空,不允许删除");
        menuService.deleteMenu(id);
        return new Result().setMessage("删除成功");
    }

    /**
     * 添加菜单
     */
    @PostMapping("saveMenu")
    public Result saveMenu(@Valid @RequestBody Menu menu, Errors errors) {
        if (errors.hasErrors()) {
            return new Result().setError(ErrorUtil.getErrors(errors));
        }
        if (menuService.getMenuByTitle(menu.getTitle()) != null) {
            return new Result().setMessage("角色title已存在");
        }
        if (menuService.getMenuByCode(menu.getCode()) != null) {
            return new Result().setMessage("角色code已存在");
        }
        if (menuService.getMenuById(menu.getParentId()) == null) {
            return new Result().setMessage("父级不存在");
        }
        menuService.addMenu(menu);
        return new Result().setMessage("新增成功");
    }
}
