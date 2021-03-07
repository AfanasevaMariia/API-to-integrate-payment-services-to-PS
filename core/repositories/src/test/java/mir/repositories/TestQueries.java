package mir.repositories;

import mir.config.TestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes=TestConfig.class)
public class TestQueries {

    //TODO: tests.
//    private final IMessageRepository repo;
//
//    @Autowired
//    TestQueries(IMessageRepository repo){
//        this.repo = repo;
//    }
//
//    @DisplayName("Get all with same mti query test")
//    @Test
//    void testMTIQuery() {
//        var res = repo.getByMti("0110");
//        System.out.println(res.size());
//    }
}

