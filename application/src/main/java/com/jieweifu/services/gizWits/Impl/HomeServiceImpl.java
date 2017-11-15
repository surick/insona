package com.jieweifu.services.gizWits.Impl;

import com.jieweifu.common.business.OperateHandler;
import com.jieweifu.common.dbservice.DB;
import com.jieweifu.models.gizWits.Home;
import com.jieweifu.services.gizWits.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomeServiceImpl implements HomeService {
    private DB db;

    @Autowired
    public HomeServiceImpl(DB db) {
        this.db = db;
    }

    /**
     * 新增
     */
    @Override
    public void saveHome(Home home) {
        OperateHandler.assignCreateUser(home);
        db.insert()
                .save(home)
                .execute();
    }

    /**
     * 修改
     */
    @Override
    public void updateHome(Home home) {
        OperateHandler.assignUpdateUser(home);
        db.update()
                .save(home)
                .execute();
    }

    /**
     * 删除
     */
    @Override
    public void removeHome(Integer id) {
        db.update()
                .table(Home.class)
                .set("is_deleted", 1)
                .where("id = ?", id)
                .execute();
    }

    /**
     * 查询
     */
    @Override
    public List<Home> AllHome() {
        return db.select()
                .from(Home.class)
                .where("is_deleted = ?", 0)
                .queryForList(Home.class);
    }

    /**
     * 按title查询
     */
    @Override
    public Home getHomeByTitle(String title) {
        return db.select()
                .from(Home.class)
                .where("is_deleted = ? AND title = ?", 0, title)
                .queryForEntity(Home.class);
    }

    /**
     * 按id查询
     */
    @Override
    public Home getHomeById(Integer id) {
        return db.select()
                .from(Home.class)
                .where("is_deleted = ? AND id = ?", 0, id)
                .queryForEntity(Home.class);
    }
}
