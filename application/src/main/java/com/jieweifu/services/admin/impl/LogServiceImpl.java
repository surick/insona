package com.jieweifu.services.admin.impl;

import com.jieweifu.common.business.BaseContextHandler;
import com.jieweifu.common.business.OperateHandler;
import com.jieweifu.common.dbservice.DB;
import com.jieweifu.models.admin.Element;
import com.jieweifu.models.admin.Log;
import com.jieweifu.models.admin.Menu;
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
        Log log = new Log();
        log.setActionTime(BaseContextHandler.getActionEndTime().toEpochMilli() - BaseContextHandler.getActionStartTime().toEpochMilli() + "ms");
        log.setUri(path);
        OperateHandler.assignCreateUser(log);
        new Thread(() -> {
            TitleName titleName = new TitleName();
            db.select()
                    .columns("menu.title, element.element_name as elementName, element.type")
                    .from(Element.class, "element")
                    .leftOuterJoin(Menu.class, "menu", "element.menu_id = menu.id")
                    .where("element.path = ? AND element.method = ?", path, method)
                    .queryForList(p -> {
                        titleName.title = p.getString(p.findColumn("title"));
                        titleName.elementName = p.getString(p.findColumn("elementName"));
                        titleName.type = p.getString(p.findColumn("type"));
                    });
            if (titleName.title == null) {
                return;
            }
            log.setMenu(titleName.title);
            log.setOpt(log.getCreateUserName()
                    .concat(method)
                    .concat(titleName.title)
                    .concat(titleName.elementName)
                    .concat(titleName.type)
                    .concat(", ")
                    .concat(hasAuthorization ? "正常" : "无权限")
                    .concat(", 响应时间")
                    .concat(log.getActionTime()));
            db.insert()
                    .save(log)
                    .execute();
        }).start();
    }

    class TitleName {
        String title;
        String elementName;
        String type;
    }
}
