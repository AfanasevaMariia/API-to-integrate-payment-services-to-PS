package com.mir.core.controllers;

import com.mir.core.models.Message;
import com.mir.core.services.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/message")
public class MessageController {

    private IMessageService service;

    @Autowired
    public MessageController(IMessageService service){
        this.service = service;
    }

    public List<Message> getMessages() {return service.getMessages();}


}
