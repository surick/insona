package com.jieweifu.services.insona.Impl;

import com.jieweifu.common.dbservice.DB;
import com.jieweifu.models.insona.InsonaUser;
import com.jieweifu.services.insona.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 10173 on 2018/3/12.
 */
@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private DB db;
    @Override
    public InsonaUser findInsonaUserByid(int id) {
        return db.select()
                .from(InsonaUser.class)
                .where("id=?",id)
                .queryForEntity(InsonaUser.class);
    }

    @Override
    public List<InsonaUser> findInsonaUserAll() {
        return db.select()
                .from(InsonaUser.class)
                .queryForList(InsonaUser.class);
    }

    @Override
    public void addInsoNaUser(InsonaUser insonaUser) {
        db.insert()
                .save(insonaUser)
                .execute();
    }

    @Override
    public void updateInsoNaUser(InsonaUser insonaUser) {
        db.update()
                .save(insonaUser)
                .execute();
    }

    @Override
    public void deleteInsonaUserById(int id) {
        db.delete()
                .from(InsonaUser.class)
                .where("id=?" ,id)
                .execute();
    }
}
