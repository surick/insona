package com.jieweifu.services.insona;

import com.jieweifu.models.insona.Log;
import com.jieweifu.models.insona.Product;

import java.util.List;

public interface ProductService {

    Product getByDid(String did);

    List<Log> getLog(String did);

    List<Product> getProducts();
}
