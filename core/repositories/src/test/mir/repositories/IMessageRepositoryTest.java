package mir.repositories;

import mir.models.ParsedMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {IMessageRepository.class})
class IMessageRepositoryTest {

    @Autowired
    IMessageRepository repository;

    @Test
    void testMti(){
        ParsedMessage message = new ParsedMessage();
        message.setMti("Mti");
        repository.save(message);
        List<ParsedMessage> Mti = repository.findAllByMti("Mti");
        assertEquals(Mti.size(),1);
    }

    @Test
    void testTransactionNumber(){
        ParsedMessage message = new ParsedMessage();
        String mockNumber = "ivewvnhewvjiow";
        message.setTransactionNumber(mockNumber);
        repository.save(message);
        ParsedMessage byTransactionNumber = repository.findByTransactionNumber(mockNumber);
        assertEquals(byTransactionNumber.getTransactionNumber(), mockNumber);
    }
    @Test
    void testHex(){
        ParsedMessage message = new ParsedMessage();
        String mockHex = "vewvwebvewbrw";
        message.setHex(mockHex);
        repository.save(message);
        List<ParsedMessage> allByHex = repository.findAllByHex(mockHex);
        assertEquals(allByHex.size(),1);
    }
    @Test
    void testById(){
        ParsedMessage message = new ParsedMessage();
        int id = -1;
        message.setId(id);
        repository.save(message);
        Optional<ParsedMessage> byId = repository.findById(id);
        assertTrue(byId.isPresent());
        assertEquals(byId.get().getId(),-1);
    }
    @Test
    void testMtiDelete(){
        ParsedMessage message = new ParsedMessage();
        String mti = "Mti";
        message.setMti(mti);
        repository.save(message);
        repository.deleteAllByMti(mti);
        List<ParsedMessage> Mti = repository.findAllByMti(mti);
        assertEquals(Mti.size(),0);
    }

    @Test
    void testHexDelete(){
        ParsedMessage message = new ParsedMessage();
        String mockHex = "vewvwebvewbrw";
        message.setHex(mockHex);
        repository.save(message);
        repository.deleteAllByHex(mockHex);
        List<ParsedMessage> allByHex = repository.findAllByHex(mockHex);
        assertEquals(allByHex.size(),0);
    }
    @Test
    void testByIdDelete(){
        ParsedMessage message = new ParsedMessage();
        int id = -1;
        message.setId(id);
        repository.save(message);
        repository.deleteById(id);
        Optional<ParsedMessage> byId = repository.findById(id);
        assertTrue(byId.isEmpty());
    }
}