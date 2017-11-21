package com.jieweifu.services.insona.Impl;

import com.jieweifu.common.dbservice.DB;
import com.jieweifu.models.insona.Material;
import com.jieweifu.services.insona.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaterialServiceImpl implements MaterialService {

    private DB db;

    @Autowired
    public MaterialServiceImpl(DB db) {
        this.db = db;
    }

    /**
     * 新增
     */
    @Override
    public void saveMaterial(Material material) {
        db.insert()
                .save(material)
                .execute();
    }

    /**
     * 删除
     */
    @Override
    public void removeMaterial(Integer id) {
        db.delete()
                .from(Material.class)
                .where("id = ?", id)
                .execute();
    }

    /**
     * 分页查询
     */
    @Override
    public List<Material> listMaterial(int pageIndex, int pageSize) {
        return db.select()
                .from(Material.class)
                .where("is_deleted = ?", 0)
                .limit(pageIndex, pageSize)
                .queryForList(Material.class);
    }

    /**
     * id查询
     */
    @Override
    public Material getMaterial(Integer id) {
        return db.select()
                .from(Material.class)
                .where("id = ? AND is_deleted = ?", id, 0)
                .queryForEntity(Material.class);
    }

    /**
     * 修改
     */
    @Override
    public void updateMaterial(Material material) {
        db.update()
                .save(material)
                .execute();
    }

    @Override
    public int getMaterialTotal() {
        return db.select()
                .from(Material.class)
                .where("is_deleted = ?", 0)
                .total();
    }
}
