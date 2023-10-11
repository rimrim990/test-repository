package learn.java.testrepository.spring.circuit;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;

@SpringBootTest
@DisplayName("스프링 서킷브레이커가 적용된 BookService 테스트")
class BookServiceTest {

    @Autowired
    ReactiveCircuitBreakerFactory breakerFactory;

    @Test
    @DisplayName("API 호출에 성공하면 정상 응답을 반환한다")
    void circuitBreaker_close() {
        // given
        final String successUrl = "http://localhost:8080";
        final BookService bookService = new BookService(breakerFactory, successUrl);

        // when
        final String result = bookService.readingList().block();

        // then
        assertThat(result).isEqualTo("외부 API 호출 성공");
    }

    @Test
    @DisplayName("API 호출에 실패하면 에러 응답을 반환한다")
    void circuitBreaker_open() {
        // given
        final String failUrl = "http://localhost:9090";
        final BookService bookService = new BookService(breakerFactory, failUrl);

        // when
        final String result = bookService.readingList().block();

        // then
        assertThat(result).isEqualTo("외부 API 호출 실패");
    }
}