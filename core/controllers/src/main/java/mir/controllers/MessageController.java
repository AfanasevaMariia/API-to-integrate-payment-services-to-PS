package mir.controllers;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import mir.models.ParsedMessage;
import mir.services.IMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class MessageController {

    private final IMessageService service;

    @Autowired
    public MessageController(IMessageService service) {
        this.service = service;
    }

    @GetMapping(path = "/welcome")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Client successfully saved"),
            @ApiResponse(code = 400, message = "The user already exists")
    })
    public String welcome(){
        return "Welcome to Mir-Core";
    }

    @GetMapping(path = "/messages")
    public List<ParsedMessage> getMessages() {
        return service.getMessages();
    }

//    // TODO: 3/6/2021 Will be removed
//    @Deprecated
//    @PostMapping
//    public void addMessage(@RequestBody ParsedMessage parsedMessage) {
//        service.addMessage(parsedMessage);
//    }
//
//    // TODO: 3/6/2021 Will be removed
//    @Deprecated
//    @DeleteMapping(path = "{id}")
//    public void deleteMessage(@PathVariable("id") Integer id) {
//        service.deleteMessageById(id);
//    }
//
//    // TODO: 3/6/2021 Will be removed
//    @Deprecated
//    @PutMapping(path = "{id}")
//    public void updateMessage(@PathVariable("id") Integer id,
//                              @RequestBody ParsedMessage parsedMessage) {
//        service.updateMessage(id, parsedMessage);
//    }
}
