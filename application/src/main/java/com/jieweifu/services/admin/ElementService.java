package com.jieweifu.services.admin;

import com.jieweifu.models.admin.Element;

import java.util.List;

public interface ElementService {

    List<Element> listElement(Integer menuId);

    Element getElement(String code);
}
