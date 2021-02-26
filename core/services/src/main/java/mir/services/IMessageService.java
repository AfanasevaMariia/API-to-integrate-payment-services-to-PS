package mir.services;

import mir.models.ParsedMessage;

import java.util.List;

public interface IMessageService {

    List<ParsedMessage> getMessages();

    void addMessage(ParsedMessage parsedMessage);

    void deleteMessageById(Integer id);

    void updateMessage(Integer id, String text);
}
