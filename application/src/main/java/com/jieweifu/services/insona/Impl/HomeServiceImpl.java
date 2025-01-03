package com.jieweifu.services.insona.Impl;

import com.jieweifu.common.business.OperateHandler;
import com.jieweifu.common.dbservice.DB;
import com.jieweifu.common.utils.RedisUtil;
import com.jieweifu.models.insona.Home;
import com.jieweifu.services.insona.HomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class HomeServiceImpl implements HomeService {
    private DB db;
    private RedisUtil redisUtil;

    @Autowired
    public HomeServiceImpl(DB db,RedisUtil redisUtil) {
        this.db = db;
        this.redisUtil = redisUtil;
    }

    /**
     * 新增
     */
    @Override
    public void saveHome(Home home) {
        home.setCreateUserName((String) redisUtil.get("userName"));
        home.setCreateTime(String.valueOf(Instant.now().toEpochMilli()));
        db.insert()
                .save(home)
                .execute();
    }

    /**
     * 修改
     */
    @Override
    public void updateHome(Home home) {
        home.setUpdateUserName((String) redisUtil.get("userName"));
        home.setUpdateTime(String.valueOf(Instant.now().toEpochMilli()));
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

    @Override
    public List<Home> homePage(int pageIndex, int pageSize) {
        return db.select()
                .from(Home.class)
                .where("is_deleted = ?", 0)
                .limit(pageIndex, pageSize)
                .queryForList(Home.class);
    }

    @Override
    public int getHomeTotal() {
        return db.select()
                .from(Home.class)
                .where("is_deleted = ?", 0)
                .total();
    }
}
