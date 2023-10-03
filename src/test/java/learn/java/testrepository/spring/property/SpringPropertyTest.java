package learn.java.testrepository.spring.property;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Map;
import java.util.Properties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("hello")
@DisplayName("스프링 프로퍼티 학습 테스트")
class SpringPropertyTest {

    @Autowired
    Environment environment;

    @Value("${PATH}")
    private String path;

    @Autowired
    ApplicationContext context;

    @Test
    @DisplayName("스프링은 Environment 구현체에 생성된 프로퍼티들을 관리한다")
    void environment() {
        // when
        final Boolean consoleProperty = environment.getProperty("spring.h2.console.enabled", Boolean.class);

        // then
        assertThat(consoleProperty).isTrue();
    }

    @Test
    @DisplayName("애플리케이션 컨텍스트는 활성화된 프로파일에 해당하는 프로퍼티 빈만 생성한다")
    void context_activeProfileBeans() {
        // when
        String goodBean = (String) context.getBean("good");

        // then
        assertThat(goodBean).isEqualTo("good bean");
        assertThatThrownBy(() -> context.getBean("hello"))
            .isInstanceOf(NoSuchBeanDefinitionException.class);
    }

    @Test
    @DisplayName("기본 활성 프로파일 값은 default 이다")
    void defaultProfile() {
        // when
        final String[] defaultProfiles = environment.getDefaultProfiles();

        // when
        assertThat(defaultProfiles).hasSize(1);
        assertThat(defaultProfiles[0]).isEqualTo("default");
    }

    @Test
    @DisplayName("ActiveProfile 로 활성 프로파일을 추가할 수 있다")
    void activeProfiles() {
        // when
        final String[] activeProfiles = environment.getActiveProfiles();

        // then
        assertThat(activeProfiles).hasSize(1);
        assertThat(activeProfiles[0]).isEqualTo("hello");
    }

    @Test
    @DisplayName("활성 프로파일에 대응되는 .properties 리소스를 읽어온다")
    void activeProfiles_propertyFiles() {
        // when
        // application-hello.properties
        final String usernameProperty = environment.getProperty("hi.hello");

        // then
        assertThat(usernameProperty).isEqualTo("hello");
    }

    @Test
    @DisplayName("활성 프로파일에 대응되는 .properties 리소스가 application.properties 보다 우선순위를 갖는다")
    void activeProfiles_priority() {
        // when
        // application-hello.properties
        final String testProperty = environment.getProperty("test.test");

        // then
        assertThat(testProperty).isEqualTo("hi");
        assertThat(testProperty).isNotEqualTo("default");
    }

    @Test
    @DisplayName("OS 환경변수 프로퍼티 가져오기")
    void osEnvironmentProperty() {
        // when
        final Map<String, String> osEnv = System.getenv();

        // then
        assertThat(osEnv).isNotEmpty();
        assertThat(osEnv).containsKey("PATH");
    }

    @Test
    @DisplayName("JVM 시스템 프로퍼티 가져오기")
    void systemProperty() {
        // when
        final Properties properties = System.getProperties();

        // then
        assertThat(properties).isNotEmpty();
        assertThat(properties).containsKey("java.class.path");
    }

    @Test
    @DisplayName("@Value 애너테이션으로 Environment 등록된 프로퍼티를 주입받을 수 있다")
    void value() {
        // then
        assertThat(path).isNotEmpty();
    }

    @Test
    @DisplayName("PropertySource 를 사용하여 프로퍼티 파일을 추가로 읽어올 수 있다")
    void propertySource() {
        // when
        final String sourceBean = (String) context.getBean("sourceBean");

        // then
        assertThat(sourceBean).isEqualTo("source");
    }
}