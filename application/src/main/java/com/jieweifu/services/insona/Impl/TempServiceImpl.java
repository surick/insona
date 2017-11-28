package com.jieweifu.services.insona.Impl;

import com.jieweifu.common.dbservice.DB;
import com.jieweifu.models.insona.Temp;
import com.jieweifu.services.insona.TempService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TempServiceImpl implements TempService {
    private DB db;

    @Autowired
    public TempServiceImpl(DB db) {
        this.db = db;
    }

    @Override
    public void saveTemp(Temp temp) {
        db.insert()
                .save(temp);
    }

    @Override
    public Temp getTemp(String did) {
        return db.select()
                .from(Temp.class)
                .where("did = ?", did)
                .queryForEntity(Temp.class);
    }

    @Override
    public void updateTemp(Temp temp) {
        db.update().save(temp);
    }
}
