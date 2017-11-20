package com.jieweifu.services.insona.Impl;

import com.jieweifu.common.dbservice.DB;
import com.jieweifu.models.insona.Product;
import com.jieweifu.models.insona.ProductInfo;
import com.jieweifu.models.insona.UserProduct;
import com.jieweifu.services.insona.UserProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserProductServiceImpl implements UserProductService {
    private DB db;

    @Autowired
    public UserProductServiceImpl(DB db) {
        this.db = db;
    }

    @Override
    public List<ProductInfo> pageUserProduct(int pageIndex, int pageSize) {
        return db.select()
                .columns("B.id,B.did,A.insona_online,A.is_disabled,A.dev_alias,B.base_user_id")
                .from(UserProduct.class, "B")
                .leftOuterJoin(Product.class, "A", "A.did = B.did")
                .limit(pageIndex, pageSize)
                .queryForList(ProductInfo.class);
    }

    @Override
    public void saveUserProduct(UserProduct userProduct) {
        userProduct.setUpdateDt(String.valueOf(System.currentTimeMillis()));
        db.insert()
                .save(userProduct)
                .execute();
    }

    @Override
    public void removeUserProduct(Integer id) {
        db.delete()
                .from(UserProduct.class)
                .where("id = ?", id)
                .execute();
    }

    @Override
    public UserProduct getByDid(String did) {
        return db.select()
                .from(UserProduct.class)
                .where("did = ?", did)
                .queryForEntity(UserProduct.class);
    }

    @Override
    public int getTotal() {
        return db.select()
                .from(UserProduct.class)
                .total();
    }

    @Override
    public UserProduct getByid(Integer id) {
        return db.select()
                .from(UserProduct.class)
                .where("id = ?", id)
                .queryForEntity(UserProduct.class);
    }

    @Override
    public List<ProductInfo> getByUid(String uid, int pageIndex, int pageSize) {
        return db.select()
                .columns("B.id,B.did,A.insona_online,A.is_disabled,A.dev_alias,B.base_user_id")
                .from(Product.class, "A")
                .leftOuterJoin(UserProduct.class, "B", "A.did = B.did")
                .where("B.base_user_id = ?", uid)
                .limit(pageIndex, pageSize)
                .queryForList(ProductInfo.class);
    }
}
