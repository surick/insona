package com.jieweifu.services.insona;

import com.jieweifu.models.insona.Temp;

public interface TempService {

    void saveTemp(Temp temp);

    Temp getTemp(String did);

    void updateTemp(Temp temp);
}
