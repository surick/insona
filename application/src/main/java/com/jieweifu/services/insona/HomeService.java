package com.jieweifu.services.insona;

import com.jieweifu.models.insona.Home;

import java.util.List;

public interface HomeService {

    void saveHome(Home home);

    void updateHome(Home home);

    void removeHome(Integer id);

    List<Home> AllHome();

    Home getHomeByTitle(String title);

    Home getHomeById(Integer id);

    List<Home> homePage(int pageIndex, int pageSize);

    int getHomeTotal();
}
