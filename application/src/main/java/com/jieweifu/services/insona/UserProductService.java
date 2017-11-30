package com.jieweifu.services.insona;

import com.jieweifu.models.insona.ProductInfo;
import com.jieweifu.models.insona.UserProduct;

import java.util.List;

public interface UserProductService {

    ProductInfo pageUserProduct(int pageIndex, int pageSize, String id);

    void saveUserProduct(UserProduct userProduct);

    void removeUserProduct(Integer id);

    UserProduct getByDid(String did);

    int getTotal();

    UserProduct getByid(Integer id);

    List<ProductInfo> getByUid(String uid, int pageIndex, int pageSize);

    List<UserProduct> listUserProduct(String uid);
}
