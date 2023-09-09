package learn.java.testrepository.spring.aop;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("트랜잭션 AOP 구현체 생성 테스트")
@SpringBootTest
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    @DisplayName("비즈니스 로직 전, 후로 트랜잭션이 적용된 UserServiceTx 를 주입받는다")
    void di_userServiceTx() {
        assertThat(userService).isExactlyInstanceOf(UserServiceTx.class);
    }
}