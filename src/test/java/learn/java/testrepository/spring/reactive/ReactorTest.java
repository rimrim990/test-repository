package learn.java.testrepository.spring.reactive;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

@DisplayName("Reactor 학습 테스트")
public class ReactorTest {

    @Test
    @DisplayName("리액터 API 사용하여 Publisher 구현하기")
    void flux() {
        Flux.create(e -> {
            e.next(1);
            e.next(2);
            e.next(3);
            e.complete();
        })
        .log()
        .subscribe(System.out::println);
    }

    @Test
    @DisplayName("리액터 API 사용하여 Map 연산자 구현하기")
    void flux_map() {
        Flux.create(e -> {
                e.next(1);
                e.next(2);
                e.next(3);
                e.complete();
            })
            .log()
            .map(a -> (int) a*10)
            .log()
            .subscribe(System.out::println);
    }
}
