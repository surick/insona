package com.jieweifu.services.admin.impl;

import com.jieweifu.common.dbservice.DB;
import com.jieweifu.models.admin.Element;
import com.jieweifu.services.admin.ElementService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ElementServiceImpl implements ElementService {
    private DB db;

    public ElementServiceImpl(DB db) {
        this.db = db;
    }

    @Override
    public List<Element> listElement(Integer menuId) {
        return db.select()
                .from(Element.class)
                .where("menu_id = ?", menuId)
                .queryForList(Element.class);
    }

    @Override
    public Element getElement(String code) {
        return db.select()
                .from(Element.class)
                .where("code = ?", code)
                .queryForEntity(Element.class);
    }

}
