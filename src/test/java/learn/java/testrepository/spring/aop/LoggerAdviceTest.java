package learn.java.testrepository.spring.aop;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("스프링 AOP 학습 테스트")
@SpringBootTest
class LoggerAdviceTest {

    @Autowired
    private LoggedService service;

    @Test
    @DisplayName("@Around 로 전반적인 메서드 실행 과정에 부가 기능을 적용할 수 있다")
    void aop_around_returning() {
        service.sayHi();
    }

    @Test
    @DisplayName("@Around 로 전반적인 메서드 실행 과정에 부가 기능을 적용할 수 있다")
    void aop_around_throwing() {
        // then
        assertThrows(RuntimeException.class, () -> service.throwRuntimeEx());
    }

    @Test
    @DisplayName("final 메서드는 오버라이딩이 불가능하므로 AOP 가 작용하지 않는다")
    void aop_finalMethod() {
        service.finalMethod();
    }
}