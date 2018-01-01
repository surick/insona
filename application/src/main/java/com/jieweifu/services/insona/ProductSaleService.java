package com.jieweifu.services.insona;

import com.jieweifu.models.insona.Product;
import com.jieweifu.models.insona.ProductSale;

import java.util.List;

public interface ProductSaleService {

    void passProduct(int id);

    void backProduct(int id,String reason);

    void updateProduct(ProductSale productSale);

    List<ProductSale> getList(int pageIndex,int pageSize);

    int total();

    List<Product> getPass(int pageIndex,int pageSize,int userId);

    int passTotal();

    List<Product> getBack(int pageIndex,int pageSize);

    int backTotal();

    void saveProduct(ProductSale productSale);

    Product getProduct(int id);
}
