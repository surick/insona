package com.jieweifu.services.insona.Impl;

import com.jieweifu.common.dbservice.DB;
import com.jieweifu.models.insona.Product;
import com.jieweifu.models.insona.ProductSale;
import com.jieweifu.services.insona.ProductSaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductSaleServiceImpl implements ProductSaleService {

    private DB db;

    @Autowired
    public ProductSaleServiceImpl(DB db) {
        this.db = db;
    }

    @Override
    public void passProduct(int id) {
        db.update()
                .table(Product.class)
                .set("status", 3)
                .where("id = ?", id)
                .execute();
    }

    @Override
    public void backProduct(int id, String reason) {
        db.update().table(Product.class)
                .set("status", 2).set("reason", reason).where("id = ?", id).execute();
    }

    @Override
    public void updateProduct(ProductSale productSale) {
        db.update().save(productSale).execute();
    }

    @Override
    public List<ProductSale> getList(int pageIndex, int pageSize,String dealer) {
        return db.select()
                .from(ProductSale.class)
                .where("dealer = ?",dealer)
                .limit(pageIndex, pageSize)
                .queryForList(ProductSale.class);
    }

    @Override
    public int total(String dealer) {
        return db.select()
                .from(ProductSale.class)
                .where("dealer = ?",dealer)
                .total();
    }

    @Override
    public List<Product> getPass(int pageIndex, int pageSize, String name) {
        return db.select().from("insona_product AS p","insona_product_dealer AS d")
                .where("p.id = d.product_id AND d.dealer = '"+name+"' AND p.status = 1")
                .limit(pageIndex,pageSize)
                .queryForList(Product.class);
    }

    @Override
    public int passTotal(String name) {
        return db.select().from("insona_product AS p","insona_product_dealer AS d")
                .where("p.id = d.product_id AND d.dealer = '"+name+"' AND p.status = 1")
                .total();
    }

    @Override
    public List<Product> getBack(int pageIndex, int pageSize,String name) {
        return db.select().from("insona_product AS p","insona_product_dealer AS d")
                .where("p.id = d.product_id AND d.dealer = '"+name+"' AND p.status = 2")
                .limit(pageIndex, pageSize)
                .queryForList(Product.class);
    }

    @Override
    public int backTotal(String name) {
        return db.select().from("insona_product AS p","insona_product_dealer AS d")
                .where("p.id = d.product_id AND d.dealer = '"+name+"' AND p.status = 2")
                .total();
    }

    @Override
    public void saveProduct(ProductSale productSale) {
        db.insert().save(productSale).execute();
    }

    @Override
    public Product getProduct(int id) {
        return db.select().from(Product.class)
                .where("id = ?", id)
                .queryForEntity(Product.class);
    }

    @Override
    public List<ProductSale> getList(String dealer) {
        return db.select()
                .from(ProductSale.class)
                .where("dealer = ?",dealer)
                .queryForList(ProductSale.class);
    }
}
