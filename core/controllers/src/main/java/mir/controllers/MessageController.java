package mir.controllers;

import mir.models.ParsedMessage;
import mir.services.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping
public class MessageController {

    private final IMessageService service;

    @Autowired
    public MessageController(IMessageService service) {
        this.service = service;
    }

    @GetMapping
    public String welcome(){
        return "Welcome to Mir-Core";
    }

    @GetMapping(path = "/messages")
    public List<ParsedMessage> getMessages() {
        return service.getMessages();
    }

    @PostMapping
    public void addMessage(@RequestBody ParsedMessage parsedMessage) {
        service.addMessage(parsedMessage);
    }

    @DeleteMapping(path = "{id}")
    public void deleteMessage(@PathVariable("id") Integer id) {
        service.deleteMessageById(id);
    }

    @PutMapping(path = "{id}")
    public void updateMessage(@PathVariable("id") Integer id,
                              @RequestBody ParsedMessage parsedMessage) {
        service.updateMessage(id, parsedMessage);
    }
}
