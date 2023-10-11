package learn.java.testrepository.spring.circuit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.ReactiveCircuitBreakerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class BookService {

    private final WebClient webClient;
    private final ReactiveCircuitBreaker readingListCircuitBreaker;

    public BookService(final ReactiveCircuitBreakerFactory circuitBreakerFactory, final String url) {
        this.webClient = WebClient.builder().baseUrl(url).build();
        this.readingListCircuitBreaker = circuitBreakerFactory.create("recommended");
    }

    public Mono<String> readingList() {
        return readingListCircuitBreaker.run(
            // API 정상 호출
            webClient.get().uri("/recommend")
                .retrieve().bodyToMono(String.class), throwable -> {
            // API 호출 실패
            log.warn("Error making request to book service", throwable);
            return Mono.just("외부 API 호출 실패");
        });
    }
}
