package learn.java.testrepository.spring.property;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("hello")
@Configuration
public class HelloProfileConfig {

    @Bean
    public String good() {
        return "good bean";
    }
}
