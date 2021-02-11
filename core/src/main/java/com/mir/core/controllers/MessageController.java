package com.mir.core.controllers;

import com.mir.core.models.Message;
import com.mir.core.services.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/message")
public class MessageController {

    private IMessageService service;

    @Autowired
    public MessageController(IMessageService service){
        this.service = service;
    }

    @GetMapping
    public List<Message> getMessages() {return service.getMessages();}

    @PostMapping
    public void addMessage(@RequestBody Message message){
        service.addMessage(message);
    }

    @DeleteMapping(path = "{id}")
    public void deleteMessage(@PathVariable("id") Integer id){
        service.deleteMessageById(id);
    }

    @PutMapping(path = "{id}")
    public void updateMessage(@PathVariable("id") Integer id, @RequestParam(required = false) String text){
        service.updateMessage(id, text);
    }
}
