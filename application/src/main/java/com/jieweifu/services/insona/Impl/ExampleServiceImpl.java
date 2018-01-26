package com.jieweifu.services.insona.Impl;

import com.jieweifu.common.dbservice.DB;
import com.jieweifu.models.insona.Example;
import com.jieweifu.services.insona.ExampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExampleServiceImpl implements ExampleService {

    private DB db;

    @Autowired
    public ExampleServiceImpl(DB db) {
        this.db = db;
    }

    @Override
    public void save(Example example) {
        if(get()!=null){
            db.update().save(example).execute();
            return;
        }
        db.insert().save(example).execute();
    }

    @Override
    public Example get() {
        return db.select()
                .from(Example.class)
                .where("id = 1")
                .queryForEntity(Example.class);
    }
}
