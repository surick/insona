package com.jieweifu.services.insona;

import com.jieweifu.models.insona.Type;

import java.util.List;

public interface TypeService {

    void saveType(Type type);

    List<Type> listTypes(int pageIndex, int pageSize);

    Type getTypeById(String type_id);

    void updateType(Type type);

    void removeType(String type_id);

    int getTotal();

    List<Type> types();

    int newTotal(String type_name);
}
