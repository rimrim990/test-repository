package learn.java.testrepository.spring.reactive;

import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("자바 PubSub 학습 테스트")
public class PubSubTest {

    @Test
    @DisplayName("Publisher 와 Subscriber 를 등록하여 이벤트를 구독할 수 있다")
    void pubsub() {
        // given
        Iterable<Integer> iter = Arrays.asList(1, 2, 3, 4, 5);

        final Publisher<Integer> pub = new Publisher<Integer>() {
            @Override
            public void subscribe(Subscriber<? super Integer> subscriber) {
                Iterator<Integer> it = iter.iterator();

                // Sub 의 onSubscribe 콜백 메서드를 실행해 주어야 한다
                subscriber.onSubscribe(new Subscription() {
                    @Override
                    public void request(long n) {
                        while(n-- > 0) {
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
}
