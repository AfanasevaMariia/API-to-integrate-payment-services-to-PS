package com.mir.core.services;

import com.mir.core.models.Message;

import java.util.List;

public interface IMessageService {

    List<Message> getMessages();

    void addMessage(Message message);

    void deleteMessageById(Integer id);

    void updateMessage(Integer id, String text);
}
