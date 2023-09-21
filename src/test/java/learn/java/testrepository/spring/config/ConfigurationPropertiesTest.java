package learn.java.testrepository.spring.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@DisplayName("스프링 ConfigurationProperties 학습 테스트")
class ConfigurationPropertiesTest {

    @Autowired
    private ConnectionSettings settings;

    @Test
    @DisplayName("prefix 를 갖는 프로퍼티들을 하나의 객체로 묶어 사용이 가능하다")
    void properties() {
        System.out.println(settings);
        // then
        assertThat(settings.getInetAddress()).isNotNull();
    }
}