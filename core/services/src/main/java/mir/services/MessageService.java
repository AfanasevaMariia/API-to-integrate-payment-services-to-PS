package mir.services;

import mir.models.ParsedMessage;
import mir.repositories.IMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService implements IMessageService {

    private final IMessageRepository repository;

    @Autowired
    public MessageService(IMessageRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ParsedMessage> getMessages() {
        return repository.findAll();
    }

    @Override
    public void addMessage(ParsedMessage parsedMessage) {
        parsedMessage.setTransactionDate(LocalDateTime.now());

        repository.save(parsedMessage);
    }

    @Override
    public void deleteMessageById(Integer id) {
        if (!repository.existsById(id))
            throw new IllegalStateException("ParsedMessage with " + id + " doesn't exists");

        repository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateMessage(Integer id, ParsedMessage parsedMessage) {

        var message = repository.findById(id).orElseThrow(
                () -> new IllegalStateException("ParsedMessage with " + id + " doesn't exists"));

        message.setMti(parsedMessage.getMti());
        message.setEdited(true);
        message.setTransactionDate(parsedMessage.getTransactionDate());
        message.setTransactionNumber(parsedMessage.getTransactionNumber());
    }
}
