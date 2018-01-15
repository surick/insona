package com.jieweifu.services.insona.Impl;

import com.jieweifu.common.dbservice.DB;
import com.jieweifu.models.insona.*;
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
    public List<ProductInfo> pageUserProduct(int pageIndex, int pageSize, String dealer) {
        return db.select()
                .columns("B.id,B.uid,B.did,A.name,A.insona_online,A.type,A.dealer")
                .from(UserProduct.class, "B")
                .leftOuterJoin(ProductSale.class, "A", "A.did = B.did")
                .where("A.dealer = ?", dealer)
                .limit(pageIndex, pageSize)
                .queryForList(ProductInfo.class);
    }

    @Override
    public void saveUserProduct(UserProduct userProduct) {
        userProduct.setCreatetime(String.valueOf(System.currentTimeMillis()));
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
    public int getTotal(String dealer) {
        return db.select()
                .columns("B.id,B.did,A.name,A.insona_online,A.type,A.dealer")
                .from(UserProduct.class, "B")
                .leftOuterJoin(ProductSale.class, "A", "A.did = B.did")
                .where("A.dealer = ?", dealer)
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
    public List<UserProduct> getByDealer(String dealer) {
        return db.select()
                .columns("A.*")
                .from(UserProduct.class, "A")
                .leftOuterJoin(ProductSale.class, "B", "A.did = B.did")
                .where("B.dealer = ?", dealer)
                .queryForList(UserProduct.class);
    }

    @Override
    public List<UserProduct> listUserProduct(String uid) {
        return db.select()
                .from(UserProduct.class)
                .where("uid = ?", uid)
                .queryForList(UserProduct.class);
    }

    @Override
    public List<InsonaUser> userList() {
        return db.select()
                .from(InsonaUser.class)
                .queryForList(InsonaUser.class);
    }
}
