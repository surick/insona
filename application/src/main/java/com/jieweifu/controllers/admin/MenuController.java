package com.jieweifu.controllers.admin;

import com.jieweifu.interceptors.AdminAuthAnnotation;
import com.jieweifu.models.ResultModel;
import com.jieweifu.models.admin.MenuModel;
import com.jieweifu.services.admin.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

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
     * @return
     */
    @PostMapping("getAllMenus")
    @ResponseBody
    @AdminAuthAnnotation(check = false)
    public ResultModel getAllMenus(){
        return new ResultModel().setData(menuModelTree(1));
    }

    /**
     * 递归生成Tree
     * @param cid
     * @return
     */
    public MenuModel menuModelTree(int cid) {
        //根据id获取节点对象
        MenuModel pMenuModel = menuService.getMenuById(cid);
        //查询id下的所有子节点
        List<MenuModel> cMenuModelList = menuService.getMenuByParentId(pMenuModel.getId());
        //遍历子节点
        for(MenuModel menuModel:cMenuModelList) {
            MenuModel m = menuModelTree(menuModel.getId());
            //添加一个集合,将子级放入父级集合
            pMenuModel.getChildren().add(m);
        }
        return pMenuModel;
    }


    /**
     * 根据id查找menu
     * @param id
     * @return
     */
    @PostMapping("getMenuById")
    @ResponseBody
    @AdminAuthAnnotation(check = false)
    public ResultModel getMenuById(int id){
        if(id<1)
            throw  new RuntimeException("id不合法");
        MenuModel menuModel = menuService.getMenuById(id);
        if(menuModel==null)
            throw new RuntimeException("查找的数据不存在");
        return new ResultModel().setData(menuModel);
    }

    /**
     * 更新menu信息,
     * @param menuModel
     */
    @PostMapping("updateMenu")
    @ResponseBody
    @AdminAuthAnnotation(check = false)
    public ResultModel updateMenu(MenuModel menuModel){
        boolean flag = true;
        if(menuService.getMenuById(menuModel.getId())!=null //判断分类是否存在
                &&menuModel.getTitle()!=null
                &&menuModel.getHref()!=null
                &&menuModel.getIcon()!=null){
            menuService.updateMenu(menuModel);
        }else {
            flag=false;
        }
        return new ResultModel().setMessage(flag?"更新成功":"更新失败");
    }

    /**
     * 删除菜单
     * @param id
     */
    @PostMapping("deleteMenu")
    @ResponseBody
    @AdminAuthAnnotation(check = false)
    public ResultModel deleteMenu(int id){
        if(id==1)
            throw new RuntimeException("系统管理不允许删除");
        MenuModel menuModel = menuService.getMenuById(id);
        if(menuModel==null)
            throw new RuntimeException("分类不存在");
        //分类下不为空则不允许删除
        if(menuService.getMenuByParentId(menuModel.getId())!=null)
            return new ResultModel().setMessage("分类下不为空,不允许删除");
        //删除菜单
        menuService.deleteMenu(id);
        return new ResultModel().setMessage("删除成功");
    }

    /**
     * 添加菜单
     * @param
     * @return
     */
    @PostMapping("addMenu")
    @AdminAuthAnnotation(check = false)
    @ResponseBody
    public ResultModel addMenu(MenuModel menuModel){

        //判断字段是否为空
        if(menuService.getMenuById(menuModel.getId())==null
                &&menuModel.getTitle()!=null
                &&menuModel.getHref()!=null){
            return new ResultModel().setMessage(menuService.addMenu(menuModel)==0?"新增失败":"新增成功");
        }else {
            //为空保存失败
            return new ResultModel().setMessage("字段不能为空");
        }
    }


}
