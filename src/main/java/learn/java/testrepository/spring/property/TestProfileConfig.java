package learn.java.testrepository.spring.property;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Configuration
public class TestProfileConfig {

    @Bean
    public String hello() {
        return "hello bean";
    }
}
