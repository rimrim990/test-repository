package learn.java.testrepository.spring.reactive;

import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Publisher -> Subscriber
 */
@DisplayName("자바 PubSub 학습 테스트")
public class PubSubTest {

    @Test
    @DisplayName("Publisher 와 Subscriber 를 등록하여 이벤트를 구독할 수 있다")
    void pubsub() {
        // given
        Iterable<Integer> iter = Arrays.asList(1, 2, 3, 4, 5);

        final Publisher<Integer> pub = new Publisher<>() {
            @Override
            public void subscribe(Subscriber<? super Integer> subscriber) {
                Iterator<Integer> it = iter.iterator();

                // Sub 의 onSubscribe 콜백 메서드를 실행해 주어야 한다
                subscriber.onSubscribe(new Subscription() {
                    @Override
                    public void request(long n) {
                        while (n-- > 0) {
                            // 데이터가 존재하면 다음 데이터를 전송한다
                            if (it.hasNext()) {
                                subscriber.onNext(it.next());
                            } else {
                                subscriber.onComplete();
                                break;
                            }
                        }
                    }

                    @Override
                    public void cancel() {

                    }
                });
            }
        };

        final Subscriber<Integer> sub = new Subscriber<>() {
            Subscription subscription;

            @Override
            public void onSubscribe(Subscription subscription) {
                // 구독 이벤트
                System.out.println(Thread.currentThread() + " onSubscribe " + subscription);
                this.subscription = subscription;
                // Pub 에 1개의 데이터 요청하기
                this.subscription.request(1);
            }

            @Override
            public void onNext(Integer item) {
                // 다음 데이터 받기
                System.out.println(Thread.currentThread() + " onNext " + item);
                this.subscription.request(1);
            }

            @Override
            public void onError(Throwable throwable) {
                // 예외 처리
                System.out.println(Thread.currentThread() + " onError " + throwable);

            }

            @Override
            public void onComplete() {
                // 데이터 전송 완료
                System.out.println(Thread.currentThread() + " onComplete");

            }
        };

        // when
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> pub.subscribe(sub));
        executorService.shutdown();

        System.out.println(Thread.currentThread() + " EXIT");
    }

    @Test
    @DisplayName("Stream 데이터를 가져오는 PubSub 객체 구현하기")
    void pubsub_streamData() {
        // given
        Iterable<Integer> iter = Stream.iterate(1, a -> a + 1)
            .limit(10)
            .collect(Collectors.toList());

        final Publisher<Integer> pub = sub -> sub.onSubscribe(new Subscription() {
            @Override
            public void request(long n) {
                try {
                    // 한 번의 요청에 10개의 스트림 데이터 전부 전송
                    iter.forEach(s -> sub.onNext(s));
                    // 데이터 전송 완료 이벤트
                    sub.onComplete();
                } catch (Exception ex) {
                    // 전송 중 에러 발생 이벤트
                    sub.onError(ex);
                }
            }

            @Override
            public void cancel() {

            }
        });

        final Subscriber<Integer> sub = new Subscriber<>() {
            @Override
            public void onSubscribe(Subscription subscription) {
                System.out.println("onSubscribe");
                subscription.request(Long.MAX_VALUE);
            }

            @Override
            public void onNext(Integer item) {
                System.out.println("onNext " + item);
            }

            @Override
            public void onError(Throwable throwable) {
                System.out.println("onError: "+ throwable);
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        };

        pub.subscribe(sub);
    }
}
