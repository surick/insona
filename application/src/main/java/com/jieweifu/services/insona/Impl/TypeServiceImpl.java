package com.jieweifu.services.insona.Impl;

import com.jieweifu.common.business.OperateHandler;
import com.jieweifu.common.dbservice.DB;
import com.jieweifu.models.insona.Type;
import com.jieweifu.services.insona.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeServiceImpl implements TypeService {

    private DB db;

    @Autowired
    public TypeServiceImpl(DB db) {
        this.db = db;
    }

    @Override
    public void saveType(Type type) {
        OperateHandler.assignCreateUser(type);
        db.insert().save(type).execute();
    }

    @Override
    public List<Type> listTypes(int pageIndex, int pageSize) {
        return db.select()
                .from(Type.class)
                .where("is_deleted = 0")
                .limit(pageIndex, pageSize)
                .queryForList(Type.class);
    }

    @Override
    public Type getTypeById(String type_id) {
        return db.select()
                .from(Type.class)
                .where("type_id = ? AND is_deleted = 0", type_id)
                .queryForEntity(Type.class);
    }

    @Override
    public void updateType(Type type) {
        OperateHandler.assignUpdateUser(type);
        db.update().save(type).execute();
    }

    @Override
    public void removeType(String type_id) {
        db.update()
                .table(Type.class)
                .set("is_deleted", 1)
                .where("id = ?", type_id)
                .execute();
    }

    @Override
    public int getTotal() {
        return db.select()
                .from(Type.class)
                .total();
    }

    @Override
    public List<Type> types() {
        return db.select().from(Type.class).queryForList(Type.class);
    }
}
