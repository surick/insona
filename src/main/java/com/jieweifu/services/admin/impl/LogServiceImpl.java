package com.jieweifu.services.admin.impl;

import com.jieweifu.common.business.BaseContextHandler;
import com.jieweifu.common.business.OperateHandler;
import com.jieweifu.common.dbservice.DB;
import com.jieweifu.models.admin.ElementModel;
import com.jieweifu.models.admin.LogModel;
import com.jieweifu.models.admin.MenuModel;
import com.jieweifu.services.admin.LogService;
import org.springframework.stereotype.Service;

@Service
public class LogServiceImpl implements LogService {
    private DB db;

    public LogServiceImpl(DB db) {
        this.db = db;
    }

    @Override
    public void log(String path, String method, boolean hasAuthorization) {
        LogModel logModel = new LogModel();
        logModel.setActionTime(BaseContextHandler.getActionEndTime().toEpochMilli() - BaseContextHandler.getActionStartTime().toEpochMilli() + "ms");
        logModel.setUri(path);
        OperateHandler.assignCreateUser(logModel);
        new Thread(() -> {
            TitleName titleName = db.select()
                    .columns("menu.title, element.element_name as elementName, element.type")
                    .from(ElementModel.class, "element")
                    .leftOuterJoin(MenuModel.class, "menu", "element.menu_id = menu.id")
                    .where("menu.path = ? AND element.method = ?", path, method)
                    .queryForEntity(TitleName.class);
            if (titleName == null) {
                titleName = new TitleName();
                titleName.title = "未知";
                titleName.elementName = "未知";
                titleName.type = -1;
            }
            logModel.setMenu(titleName.title);
            logModel.setOpt(logModel.getCreateUserName()
                    .concat("用户")
                    .concat("执行")
                    .concat(titleName.title)
                    .concat("菜单")
                    .concat(titleName.type == -1 ? "未知类型" : (titleName.type == 0 ? "功能" : "按钮"))
                    .concat(titleName.elementName)
                    .concat(method)
                    .concat("请求, ")
                    .concat(hasAuthorization ? "正常" : "无权限")
                    .concat(", 响应时间")
                    .concat(logModel.getActionTime()));
            db.insert()
                    .save(logModel)
                    .execute();
        }).start();
    }

    class TitleName {
        String title;
        String elementName;
        int type;
    }
}
