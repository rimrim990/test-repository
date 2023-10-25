package learn.java.testrepository.spring.reactive;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

@Slf4j
@DisplayName("리액터 스케줄러 학습 테스트")
public class SchedulerTest {

    @Test
    @DisplayName("리액터 Publisher/Subscriber 를 사용하여 이벤트 발행")
    void pubSub() {
        //  given
        Publisher<Integer> pub = getPub();

        // when
        pub.subscribe(getLogSub());
    }

    @Test
    @DisplayName("중간 연산자를 추가하여 메인 스레드를 블락킹하지 않고 Pub/Sub 수행이 가능하다")
    void async() {
        // given
        Publisher<Integer> pub = getPub();

        // when
        /**
         * Publisher 의 데이터 생성이 느릴 경우 별개의 스레드에서 실행
         */
        Publisher<Integer> asyncPub = s -> {
            final ExecutorService es = Executors.newSingleThreadExecutor();
            es.execute(() -> pub.subscribe(s));
        };

        asyncPub.subscribe(getLogSub());
        log.info("EXIT");
    }

    @Test
    @DisplayName("중간 연산자를 추가하여 Subscriber 를 별도의 스레드에서 실행 가능하다")
    void async_publishOn() {
        // given
        Publisher<Integer> pub = getPub();

        // when
        /**
         * Publisher 의 데이터 생성은 빠르지만 Subscriber 의 데이터 사용은 느릴 경우 별개의 스레드에서 실행
         */
        Publisher<Integer> asyncPub = originalSub -> {
            // 단일 스레드를 사용하므로 동시성 문제가 발생하지 않음
            final ExecutorService es = Executors.newSingleThreadExecutor();
            pub.subscribe(new Subscriber<>() {
                @Override
                public void onSubscribe(Subscription s) {
                    originalSub.onSubscribe(s);
                }

                @Override
                public void onNext(Integer integer) {
                    es.submit(() -> originalSub.onNext(integer));
                }

                @Override
                public void onError(Throwable t) {
                    es.submit(() -> originalSub.onError(t));
                    es.shutdown();
                }

                @Override
                public void onComplete() {
                    es.submit(() -> originalSub.onComplete());
                    es.shutdown();
                }
            });
        };

        asyncPub.subscribe(getLogSub());

        log.info("EXIT");
    }

    @Test
    @DisplayName("리액터를 사용하여 별도의 스레드에서 실행하기")
    void reactor_subscribeOn() {
        Flux.range(1, 10)
            .log()
            .subscribeOn(Schedulers.newSingle("sub"))
            .subscribe(System.out::println);

        log.info("EXIT");
    }

    @Test
    @DisplayName("리액터를 사용하여 별도의 스레드에서 실행하기")
    void reactor_publishOn() {
        Flux.range(1, 10)
            .publishOn(Schedulers.newSingle("pub"))
            .log()
            .subscribeOn(Schedulers.newSingle("sub"))
            .subscribe(System.out::println);

        log.info("EXIT");
    }

    @Test
    @DisplayName("일정 시간마다 Subscriber 를 호출하는 Publisher 구현")
    void async_scheduler() throws InterruptedException {
        // given
        Publisher<Integer>  scheduledPub = sub -> {
            final ScheduledExecutorService es = Executors.newSingleThreadScheduledExecutor();
            sub.onSubscribe(new Subscription() {
                int num = 0;

                @Override
                public void request(long n) {
                    es.scheduleAtFixedRate(() -> sub.onNext(num++), 0, 300, TimeUnit.MILLISECONDS);
                }

                @Override
                public void cancel() {

                }
            });
        };

        scheduledPub.subscribe(getLogSub());

        TimeUnit.SECONDS.sleep(10);
    }

    @Test
    @DisplayName("일정 시간마다 Subscriber 를 5번까지만 호출하는 Publisher 구현")
    void async_schedulerWithTake() throws InterruptedException {
        // given
        Publisher<Integer>  scheduledPub = sub -> {
            final ScheduledExecutorService es = Executors.newSingleThreadScheduledExecutor();
            sub.onSubscribe(new Subscription() {
                int num = 0;
                volatile boolean canceled = false;

                @Override
                public void request(long n) {
                    es.scheduleAtFixedRate(() -> {
                        if (canceled) {
                            es.shutdown();
                        }
                        sub.onNext(num++);
                        }, 0, 300, TimeUnit.MILLISECONDS);
                }

                @Override
                public void cancel() {
                    this.canceled = true;
                }
            });
        };

        // when
        Publisher<Integer> takePub = originalSub -> {
            scheduledPub.subscribe(new Subscriber<>() {
                int count = 0;
                Subscription subscription;

                @Override
                public void onSubscribe(Subscription s) {
                    this.subscription = s;
                    originalSub.onSubscribe(s);
                }

                @Override
                public void onNext(Integer integer) {
                    originalSub.onNext(integer);
                    if (++count >= 5) {
                        subscription.cancel();
                    }
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
        };

        takePub.subscribe(getLogSub());

        TimeUnit.SECONDS.sleep(10);
    }

    @Test
    @DisplayName("리액터 인터버로 일정 시간마다 실행하기")
    void reactor_interval() throws InterruptedException {
        Flux.interval(Duration.ofSeconds(1))
            .take(5)
            .subscribe(s -> log.info("onNext: {}", s));

        TimeUnit.SECONDS.sleep(10);
    }

    private Publisher<Integer> getPub() {
        return s -> {
            s.onSubscribe(new Subscription() {
                @Override
                public void request(long n) {
                    s.onNext(1);
                    s.onNext(2);
                    s.onNext(3);
                    s.onNext(4);
                    s.onNext(5);
                    s.onComplete();
                }

                @Override
                public void cancel() {

                }
            });
        };
    }

    private Subscriber<Integer> getLogSub() {
        return new Subscriber<>() {
            @Override
            public void onSubscribe(Subscription s) {
                // 데이터 요청하기
                log.info("onSubscribe");
                s.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Integer integer) {
                log.info("onNext: {}", integer);
            }

            @Override
            public void onError(Throwable t) {
                log.info("onError: {}", t);
            }

            @Override
            public void onComplete() {
                log.info("onComplete");
            }
        };
    }
}
