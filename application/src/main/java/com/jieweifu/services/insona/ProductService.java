package com.jieweifu.services.insona;

import com.jieweifu.models.insona.Log;
import com.jieweifu.models.insona.Product;

import java.util.List;

public interface ProductService {

    Product getByDid(String did);

    List<Log> getLog(String did);

    List<Product> getProducts();

    List<Product> getProducts(int pageIndex, int pageSize);

    void saveProduct(Product product);

    void updateProduct(Product product);

    Product getProductById(int id);

    void removeProduct(int id);

    int total();

    List<Product> listProducts(String status1, String status2);

    List<Product> listProducts(String status1);

    int total(String status);

    int total(String status1, String status2);

    void setStatus(int id, String status,String name,String sale_time);
}
