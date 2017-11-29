package com.jieweifu.services.insona;

import com.jieweifu.models.insona.Message;

import java.util.List;

public interface MessageService {

    void saveMessage(Message message);

    void removeMessage(Integer id);

    List<Message> listMessage(int pageIndex, int pageSize);

    Message getMessage(Integer id);

    void updateMessage(Message message);

    int getMessageTotal();
}
