package com.jieweifu.services.insona.Impl;

import com.jieweifu.common.dbservice.DB;
import com.jieweifu.models.insona.Table;
import com.jieweifu.services.insona.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TableServiceImpl implements TableService {

    private DB db;

    @Autowired
    public TableServiceImpl(DB db){
        this.db = db;
    }
    @Override
    public List<Table> listTables() {
        return db.select()
                .from(Table.class)
                .queryForList(Table.class);
    }
}
