package com.jieweifu.services.gizWits;

import com.jieweifu.models.gizWits.Info;

import java.util.List;

public interface InfoService {

    void updateInfo(Info info);

    List<Info> getInfo();

    Info getInfoByTitle(String title);

    List<Info> getInfoByType(Integer type);

    void saveInfo(Info info);

    Info getInfoById(Integer id);

    void removeInfo(Integer id);
}
