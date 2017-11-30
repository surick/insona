package com.jieweifu.services.insona.Impl;

import com.jieweifu.common.dbservice.DB;
import com.jieweifu.models.insona.InsonaUser;
import com.jieweifu.models.insona.Product;
import com.jieweifu.models.insona.UserProduct;
import com.jieweifu.services.insona.TerminalUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TerminalUserServiceImpl implements TerminalUserService {

    private DB db;

    @Autowired
    public TerminalUserServiceImpl(DB db) {
        this.db = db;
    }

    @Override
    public List<InsonaUser> listUser(int pageIndex, int pageSize) {
        return db.select()
                .from(InsonaUser.class)
                .limit(pageIndex, pageSize)
                .queryForList(InsonaUser.class);
    }

    @Override
    public InsonaUser getUserById(String id) {
        return db.select()
                .from(InsonaUser.class)
                .where("uid = ?", id)
                .queryForEntity(InsonaUser.class);
    }

    @Override
    public int getTotal() {
        return db.select()
                .from(InsonaUser.class)
                .total();
    }

    @Override
    public List<UserProduct> listProduct(String uid) {
        return db.select()
                .from(UserProduct.class)
                .where("base_user_id = ?", uid)
                .queryForList(UserProduct.class);
    }
}