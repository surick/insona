package com.jieweifu.services.insona.Impl;

import com.jieweifu.common.business.OperateHandler;
import com.jieweifu.common.dbservice.DB;
import com.jieweifu.models.insona.ProductDealer;
import com.jieweifu.services.insona.ProductDealerService;
import org.springframework.stereotype.Service;

@Service
public class ProductDealerServiceImpl implements ProductDealerService {

    private DB db;

    public ProductDealerServiceImpl(DB db){
        this.db = db;
    }

    @Override
    public void saveProductDealer(ProductDealer productDealer) {
        OperateHandler.assignCreateUser(productDealer);
        db.insert().save(productDealer).execute();
    }

    @Override
    public void removeProductDealer(int id) {
        db.delete().from(ProductDealer.class).where("id = ?", id).execute();
    }
}
