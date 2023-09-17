package learn.java.testrepository.spring.testContext;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase
public class ProfileContextTest {

    @Autowired
    ApplicationContext context;

    @Test
    @DisplayName("applicationContext 실행 시간 체크")
    void startUpDate() {
        System.out.println("applicationContext started at : " + context.getStartupDate());
    }
}
