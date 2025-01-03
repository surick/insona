package com.jieweifu.services.insona.Impl;

import com.jieweifu.common.business.BaseContextHandler;
import com.jieweifu.common.business.OperateHandler;
import com.jieweifu.common.dbservice.DB;
import com.jieweifu.common.utils.RedisUtil;
import com.jieweifu.models.insona.Document;
import com.jieweifu.services.insona.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Service
public class DocumentServiceImpl implements DocumentService {
    private DB db;
    private RedisUtil redisUtil;

    @Autowired
    public DocumentServiceImpl(DB db,RedisUtil redisUtil) {
        this.redisUtil = redisUtil;
        this.db = db;
    }

    /**
     * 新增
     */
    @Override
    public void saveDocument(Document document) {
        document.setCreateUserName((String) redisUtil.get("userName"));
        document.setCreateTime(String.valueOf(Instant.now().toEpochMilli()));
        db.insert()
                .save(document)
                .execute();
    }

    /**
     * 删除
     */
    @Override
    public void removeDocument(Integer id) {
        db.update()
                .table(Document.class)
                .set("is_deleted", 1)
                .where("id = ?", id)
                .execute();
    }

    /**
     * 修改
     */
    @Override
    public void updateDocument(Document document) {
        document.setUpdateUserName((String) redisUtil.get("userName"));
        document.setUpdateTime(String.valueOf(Instant.now().toEpochMilli()));
        db.update()
                .save(document)
                .execute();
    }

    /**
     * name查找
     */
    @Override
    public Document AllDocumentByName(String name) {
        return db.select()
                .from(Document.class)
                .where("is_deleted = ? AND name = ?", 0, name)
                .queryForEntity(Document.class);
    }

    /**
     * type查找
     */
    @Override
    public List<Document> AllDocumentByType(String type) {

        return db.select()
                .from(Document.class)
                .where("is_deleted = ? AND file_type = ?", 0, type)
                .queryForList(Document.class);
    }

    /**
     * 全部查找
     */
    @Override
    public List<Document> AllDocument() {
        return db.select()
                .from(Document.class)
                .where("is_deleted = ?", 0)
                .queryForList(Document.class);
    }

    /**
     * 按id查找
     */
    @Override
    public Document getDocument(Integer id) {
        return db.select()
                .from(Document.class)
                .where("is_deleted = ? AND id = ?", 0, id)
                .queryForEntity(Document.class);
    }

    @Override
    public List<Document> documentPage(int pageIndex, int pageSize,String label) {
        return db.select()
                .from(Document.class)
                .where("is_deleted = 0 AND label like ?", "%0010001%")
                .limit(pageIndex, pageSize)
                .queryForList(Document.class);
    }

    @Override
    public int getDocumentTotal() {
        return db.select()
                .from(Document.class)
                .where("is_deleted = ?", 0)
                .total();
    }

    @Override
    public List<Document> documentPages(int pageIndex, int pageSize, String label) {
        return db.select()
                .from(Document.class)
                .where("is_deleted = 0 AND label = ?", label)
                .limit(pageIndex, pageSize)
                .queryForList(Document.class);
    }

    @Override
    public List<Document> allList(int pageIndex, int pageSize) {
        return db.select()
                .from(Document.class)
                .where("is_deleted = 0")
                .limit(pageIndex, pageSize)
                .queryForList(Document.class);
    }

    @Override
    public List<Document> otherDocument(int pageIndex, int pageSize) {
        return db.select()
                .from(Document.class)
                .where("is_deleted = 0 AND label = ?", 0)
                .limit(pageIndex, pageSize)
                .queryForList(Document.class);
    }
}
