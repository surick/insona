package com.jieweifu.services.insona.Impl;

import com.jieweifu.common.dbservice.DB;
import com.jieweifu.models.insona.Log;
import com.jieweifu.models.insona.Product;
import com.jieweifu.services.insona.ProductService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<Log> getLog(String did) {
        return db.select()
                .from(Log.class)
                .where("did = ?", did)
                .queryForList(Log.class);
    }

    @Override
    public List<Product> getProducts() {
        return db.select()
                .from(Product.class)
                .queryForList(Product.class);
    }

    @Override
    public List<Product> getProducts(int pageIndex, int pageSize) {
        return db.select()
                .from(Product.class)
                .limit(pageIndex, pageSize)
                .queryForList(Product.class);
    }

    @Override
    public void saveProduct(Product product) {
        db.insert().save(product).execute();
    }

    @Override
    public void updateProduct(Product product) {
        db.update().save(product).execute();
    }

    @Override
    public Product getProductById(int id) {
        return db.select()
                .from(Product.class)
                .where("id = ?", id)
                .queryForEntity(Product.class);
    }

    @Override
    public void removeProduct(int id) {
        db.delete().from(Product.class).where("id = ?", id).execute();
    }

    @Override
    public int total() {
        return db.select().from(Product.class).total();
    }

    @Override
    public List<Product> listProducts(String status1, String status2) {
        return db.select()
                .from(Product.class)
                .where("status IN ( ? , ? )", status1, status2)
                .queryForList(Product.class);
    }

    @Override
    public List<Product> listProducts(String status) {
        return db.select()
                .from(Product.class)
                .where("status = ?", status)
                .queryForList(Product.class);
    }

    @Override
    public int total(String status) {

        return db.select()
                .from(Product.class)
                .where("status = ?", status)
                .total();
    }

    @Override
    public int total(String status1, String status2) {

        return db.select()
                .from(Product.class)
                .where("status IN ( ? , ? )", status1, status2)
                .total();
    }

    @Override
    public void setStatus(int id, String status,String name) {
        Product product = db.select().columns("sale")
                .from(Product.class)
                .where("id = ?",id)
                .queryForEntity(Product.class);
        if(!StringUtils.isBlank(product.getSale())){
            name = product.getSale()+","+name;
        }
        db.update()
                .table(Product.class)
                .set("status", status)
                .set("sale", name)
                .where("id = ?", id)
                .execute();
    }
}
