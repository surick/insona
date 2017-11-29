package com.jieweifu.services.insona.Impl;

import com.jieweifu.common.dbservice.DB;
import com.jieweifu.models.insona.Message;
import com.jieweifu.services.insona.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    private DB db;

    @Autowired
    public MessageServiceImpl(DB db) {
        this.db = db;
    }

    @Override
    public void saveMessage(Message message) {
        db.insert().save(message).execute();
    }

    @Override
    public void removeMessage(Integer id) {
        db.update()
                .table(Message.class)
                .set("is_deleted", 1)
                .where("id = ?", id)
                .execute();
    }

    @Override
    public List<Message> listMessage(int pageIndex, int pageSize) {
        return db.select()
                .from(Message.class)
                .where("is_deleted = ?", 0)
                .limit(pageIndex, pageSize)
                .queryForList(Message.class);
    }

    @Override
    public Message getMessage(Integer id) {
        return db.select()
                .from(Message.class)
                .where("id = ? AND is_deleted = 0", id)
                .queryForEntity(Message.class);
    }

    @Override
    public void updateMessage(Message message) {
        db.update().save(message).execute();
    }

    @Override
    public int getMessageTotal() {
        return db.select()
                .from(Message.class)
                .where("is_deleted = ?", 0)
                .total();
    }
}
