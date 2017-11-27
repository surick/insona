package com.jieweifu.services.insona;


import com.jieweifu.models.insona.InsonaUser;
import com.jieweifu.models.insona.UserProduct;

import java.util.List;

public interface TerminalUserService {

    List<InsonaUser> listUser(int pageIndex, int pageSize);

    InsonaUser getUserById(String id);

    int getTotal();

    List<UserProduct> listProduct(String uid);
}
