package com.jieweifu.services.insona.Impl;

import com.jieweifu.common.dbservice.DB;
import com.jieweifu.models.insona.Product;
import com.jieweifu.services.insona.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {
    private DB db;

    @Autowired
    public ProductServiceImpl(DB db) {
        this.db = db;
    }

    @Override
    public Product getByDid(String did) {
        return db.select()
                .from(Product.class)
                .where("did = ?", did)
                .queryForEntity(Product.class);
    }
}
