package com.jieweifu.services.insona;

import com.jieweifu.models.insona.Document;

import java.util.List;

public interface DocumentService {

    void saveDocument(Document document);

    void removeDocument(Integer id);

    void updateDocument(Document document);

    Document AllDocumentByName(String name);

    List<Document> AllDocumentByType(String type);

    List<Document> AllDocument();

    Document getDocument(Integer id);

    List<Document> documentPage(int pageIndex, int pageSize, String label);

    List<Document> documentPages(int pageIndex, int pageSize, String label);

    List<Document> allList(int pageIndex, int pageSize);

    int getDocumentTotal();
}
