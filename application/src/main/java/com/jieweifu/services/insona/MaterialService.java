package com.jieweifu.services.insona;

import com.jieweifu.models.insona.Material;

import java.util.List;

public interface MaterialService {

    void saveMaterial(Material material);

    void removeMaterial(Integer id);

    List<Material> listMaterial(int pageIndex, int pageSize);

    Material getMaterial(Integer id);

    void updateMaterial(Material material);

    int getMaterialTotal();
}
