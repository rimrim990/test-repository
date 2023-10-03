package learn.java.testrepository.spring.property;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:/source.properties")
public class SourcePropertyConfig {

    @Value("${source.test}")
    private String source;

    @Bean
    public String sourceBean() {
        return source;
    }
}
