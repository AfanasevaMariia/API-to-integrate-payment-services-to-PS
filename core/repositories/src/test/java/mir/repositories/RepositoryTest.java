package mir.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = IMessageRepository.class)
public class RepositoryTest {

    @MockBean
    private IMessageRepository repository;

    @Test
    void MyTest(){
        boolean result = repository != null;
    }
}
