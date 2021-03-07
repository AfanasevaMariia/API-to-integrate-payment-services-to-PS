package mir.config;

import mir.repositories.IMessageRepository;
import mir.repositories.IMessageRepositoryImpl;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration
public class TestConfig {

    @Bean
    public IMessageRepository getRepository() {
        return new IMessageRepositoryImpl();
    }
}