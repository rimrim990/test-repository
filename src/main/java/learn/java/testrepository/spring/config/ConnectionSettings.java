package learn.java.testrepository.spring.config;

import jakarta.validation.constraints.NotNull;
import java.net.InetAddress;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "connection")
public class ConnectionSettings {

    @NotNull
    private InetAddress inetAddress;
}
