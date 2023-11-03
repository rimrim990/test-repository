package learn.java.testrepository.spring.reactive;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.function.UnaryOperator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;

@Slf4j
@DisplayName("스프링 웹 플럭스 Flux 학습 테스트")
public class WebFluxTest {

    @Test
    @DisplayName("Flux.just 로 Flux 를 생성한다")
    void flux_just() {
        // 여러 데이터를 순차적으로 처리한다
        Flux.just("hello", "hi")
            .doOnNext(val -> log.info("doOnNext: {}", val))
            .subscribe(val -> log.info("subscribe: {}", val));
    }

    @Test
    @DisplayName("Flux 로 컬렉션 데이터의 스트림 처리가 가능하다")
    void flux_iterable() {
        Flux.fromIterable(List.of(1, 2, 3, 4))
            .log()
            .map(val -> val * 2)
            .log()
            .subscribe(val -> log.info("{}", val));
    }

    @Test
    @DisplayName("Flux.flatMap 으로 중첩된 Flux 를 평평하게 만들 수 있다")
    void flux_flatMap() {
        // given
        final Flux<Flux<Integer>> nestedFlux = Flux
            .fromIterable(List.of(1, 2, 3, 4))
            .map(val -> Flux.fromIterable(List.of(val)));

        // when
        final Flux<Integer> intFlux = nestedFlux.flatMap(flux -> flux.map(val -> val+1));

        // then
        intFlux.subscribe(val -> log.info(String.valueOf(val)));
    }

    @Test
    @DisplayName("Flux.filter 로 특정 원소를 필터링할 수 있다")
    void flux_filter() {
        Flux.fromIterable(List.of(1, 5, 8, 10))
            .filter(val -> val >= 5)
            .subscribe(val -> log.info(String.valueOf(val)));
    }

    @Test
    @DisplayName("Flux.defaultIfEmpty 로 empty Publisher 에 기본 값을 제공할 수 있다")
    void flux_defaultIfEmpty() {
        Flux.fromIterable(List.of(1, 5, 8, 10))
            .filter(val -> val >= 15)
            .defaultIfEmpty(15)
            .subscribe(val -> log.info(String.valueOf(val)));
    }

    @Test
    @DisplayName("Flux.zip 으로 Flux를 묶어 튜플을 생성할 수 있다")
    void flux_zip() {
        // given
        final Flux<Integer> intFlux = Flux.fromIterable(List.of(1, 2, 3, 4));
        final Flux<String> strFlux = Flux.fromIterable(List.of("hi", "hello", "bye", "good" ,"not"));

        // when
        Flux.zip(intFlux, strFlux)
            .subscribe(tup -> log.info("{}: {}", tup.getT1(), tup.getT2()));
    }

    @Test
    @DisplayName("Pub/Sub 을 사용하여 Flux.map 구현해보기")
    void flux_map_usingPubSub() {
        // given
        Publisher<String> originalPub = new Publisher<>() {
            @Override
            public void subscribe(Subscriber<? super String> subscriber) {
                Deque<String> datas = new LinkedList<>(List.of("hi", "hello", "bye"));

                subscriber.onSubscribe(new Subscription() {
                    @Override
                    public void request(long n) {
                        if (datas.isEmpty()) {
                            subscriber.onComplete();
                            return;
                        }
                        String data = datas.pop();
                        subscriber.onNext(data);
                    }

                    @Override
                    public void cancel() {

                    }
                });
            }
        };

        Subscriber<String> originalSub = new Subscriber<>() {
            @Override
            public void onSubscribe(Subscription s) {
                log.info("originalSub: onSubscribe");
                s.request(1);
            }

            @Override
            public void onNext(String s) {
                log.info("originalSub: onNext, {}", s);
            }

            @Override
            public void onError(Throwable t) {
                log.info("originalSub: onError, {}", t.getMessage());
            }

            @Override
            public void onComplete() {
                log.info("originalSub: onComplete");
            }
        };

        // when
        final UnaryOperator<String> operator = str -> str.toUpperCase();

        final Publisher<String> mapPub = new Publisher<>() {
            Subscription sub;

            @Override
            public void subscribe(Subscriber<? super String> originalSub) {
                originalPub.subscribe(new Subscriber<>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        sub = s;
                        originalSub.onSubscribe(s);
                    }

                    @Override
                    public void onNext(String s) {
                        // map
                        originalSub.onNext(operator.apply(s));
                        sub.request(1);
                    }

                    @Override
                    public void onError(Throwable t) {
                        originalSub.onError(t);
                    }

                    @Override
                    public void onComplete() {
                        originalSub.onComplete();
                    }
                });
            }
        };

        // then
        mapPub.subscribe(originalSub);
    }
}
