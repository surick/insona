package com.jieweifu.services.gizWits.Impl;

import com.jieweifu.common.business.OperateHandler;
import com.jieweifu.common.dbservice.DB;
import com.jieweifu.models.gizWits.Info;
import com.jieweifu.services.gizWits.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InfoServiceImpl implements InfoService {
    private DB db;

    @Autowired
    public InfoServiceImpl(DB db) {
        this.db = db;
    }

    /**
     * 修改
     */
    @Override
    public void updateInfo(Info info) {
        OperateHandler.assignUpdateUser(info);
        db.update()
                .save(info)
                .execute();
    }

    /**
     * 查询
     */
    @Override
    public List<Info> getInfo() {
        return db.select()
                .from(Info.class)
                .where("is_deleted = ?", 0)
                .queryForList(Info.class);
    }

    /**
     * 按title查询
     */
    @Override
    public Info getInfoByTitle(String title) {
        return db.select()
                .from(Info.class)
                .where("is_deleted = ? AND title = ?", 0, title)
                .queryForEntity(Info.class);
    }

    /**
     * 按type查询
     */
    @Override
    public List<Info> getInfoByType(Integer type) {
        return db.select()
                .from(Info.class)
                .where("is_deleted = ? AND info_type = ?", 0, type)
                .queryForList(Info.class);
    }

    /**
     * 新增
     */
    @Override
    public void saveInfo(Info info) {
        OperateHandler.assignCreateUser(info);
        db.insert()
                .save(info)
                .execute();
    }

    /**
     * 按id查询
     */
    @Override
    public Info getInfoById(Integer id) {
        return db.select()
                .from(Info.class)
                .where("is_deleted = ? AND id = ?", 0, id)
                .queryForEntity(Info.class);
    }

    /**
     * 删除
     */
    @Override
    public void removeInfo(Integer id) {
        db.update()
                .table(Info.class)
                .set("is_deleted", 1)
                .where("id = ?", id)
                .execute();
    }

    /**
     * 分页查询
     */
    @Override
    public List<Info> pageInfo(int pageIndex, int pageSize) {
        return db.select()
                .from(Info.class)
                .where("is_deleted = ?", 0)
                .limit(pageIndex, pageSize)
                .queryForList(Info.class);
    }

    /**
     * 总数
     */
    @Override
    public int getInfoTotal() {
        return db.select()
                .from(Info.class)
                .where("is_deleted = ?", 0)
                .total();
    }


}
