package com.jieweifu.services.admin.impl;

import com.jieweifu.common.dbservice.DB;
import com.jieweifu.models.admin.LogModel;
import com.jieweifu.services.admin.LogService;
import org.springframework.stereotype.Service;

@Service
public class LogServiceImpl implements LogService {
    private DB db;

    public LogServiceImpl(DB db) {
        this.db = db;
    }

    @Override
    public void log(LogModel logModel) {
        db.insert()
                .save(logModel)
                .execute();
    }
}
