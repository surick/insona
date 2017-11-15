package com.jieweifu.services.gizWits;

import com.jieweifu.models.gizWits.Document;

import java.util.List;

public interface DocumentService {

    void saveDocument(Document document);

    void removeDocument(Integer id);

    void updateDocument(Document document);

    Document AllDocumentByName(String name);

    List<Document> AllDocumentByType(String type);

    List<Document> AllDocument();

    Document getDocument(Integer id);
}
