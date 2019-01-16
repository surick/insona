package com.jieweifu.services.insona.Impl;

import com.jieweifu.common.dbservice.DB;
import com.jieweifu.models.insona.About;
import com.jieweifu.services.insona.AboutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Jin
 * @date 2018/12/10
 */
@Service
public class AboutServiceImpl implements AboutService {

    private DB db;

    @Autowired
    public AboutServiceImpl(DB db) {
        this.db = db;
    }

    @Override
    public void updateAbout(About about) {
        db.update()
                .save(about)
                .execute();
    }

    @Override
    public About getAbout(Integer id) {
        return db.select()
                .from(About.class)
                .where("id = ?", id)
                .queryForEntity(About.class);
    }
}
