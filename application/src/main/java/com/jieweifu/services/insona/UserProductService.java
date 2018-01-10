package com.jieweifu.services.insona;

import com.jieweifu.models.insona.InsonaUser;
import com.jieweifu.models.insona.ProductInfo;
import com.jieweifu.models.insona.UserProduct;

import java.util.List;

public interface UserProductService {

    List<ProductInfo> pageUserProduct(int pageIndex, int pageSize, String dealer);

    void saveUserProduct(UserProduct userProduct);

    void removeUserProduct(Integer id);

    UserProduct getByDid(String did);

    int getTotal(String dealer);

    UserProduct getByid(Integer id);

    List<UserProduct> getByDealer(String dealer);

    List<UserProduct> listUserProduct(String uid);

    List<InsonaUser> userList();
}
