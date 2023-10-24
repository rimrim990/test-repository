package learn.java.testrepository.spring.reactive;

import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Publisher -> [Data1] -> Op1 -> [Data2] -> Op2 -> [Data3] -> Subscriber
 */
@DisplayName("PubSub 오퍼레이터 학습 테스트")
public class OperatorTest {

    @Test
    @DisplayName("Map 오퍼레이터 학습 테스트")
    void map() {
        // given
        final Publisher<Integer> pub = iterPub(Stream.iterate(1, a -> a+1).limit(10).collect(Collectors.toList()));
        final Subscriber<Integer> sub = logSub();

        final Function<Integer, Integer> func = v -> v*10;

        // when
        final Publisher<Integer> mapPub = subscriber -> {
            pub.subscribe(new Subscriber<>() {
                @Override
                public void onSubscribe(Subscription subscription) {
                    subscriber.onSubscribe(subscription);
                }

                @Override
                public void onNext(Integer item) {
                    subscriber.onNext(func.apply(item));
                }

                @Override
                public void onError(Throwable throwable) {
                    subscriber.onError(throwable);
                }

                @Override
                public void onComplete() {
                    subscriber.onComplete();
                }
            });
        };

        mapPub.subscribe(sub);
    }


    @Test
    @DisplayName("Sum 오퍼레이터 학습 테스트")
    void sum() {
        // given
        final Publisher<Integer> pub = iterPub(Stream.iterate(1, a -> a+1).limit(10).collect(Collectors.toList()));
        final Subscriber<Integer> sub = logSub();

        // when
        final Publisher<Integer> sumPub = subscriber -> {
            pub.subscribe(new Subscriber<>() {
                int sum = 0;

                @Override
                public void onSubscribe(Subscription subscription) {
                    subscriber.onSubscribe(subscription);
                }

                @Override
                public void onNext(Integer item) {
                    sum += item;
                }

                @Override
                public void onError(Throwable throwable) {
                    subscriber.onError(throwable);
                }

                @Override
                public void onComplete() {
                    subscriber.onNext(sum);
                    subscriber.onComplete();
                }
            });
        };

        sumPub.subscribe(sub);
    }

    @Test
    @DisplayName("Reduce 오퍼레이터 학습 테스트")
    void reduce() {
        // given
        final Publisher<Integer> pub = iterPub(Stream.iterate(1, a -> a+1).limit(10).collect(Collectors.toList()));
        final Subscriber<Integer> sub = logSub();

        final int init = 0;
        final BiFunction<Integer, Integer, Integer> biFunc = (a, b) -> a+b;

        // when
        final Publisher<Integer> reducePub = subscriber -> {
            pub.subscribe(new Subscriber<>() {
                int result = init;

                @Override
                public void onSubscribe(Subscription subscription) {
                    subscriber.onSubscribe(subscription);
                }

                @Override
                public void onNext(Integer item) {
                    result = biFunc.apply(result, item);
                }

                @Override
                public void onError(Throwable throwable) {
                    subscriber.onError(throwable);
                }

                @Override
                public void onComplete() {
                    subscriber.onNext(result);
                    subscriber.onComplete();
                }
            });
        };

        reducePub.subscribe(sub);
    }

    private Publisher<Integer> iterPub(Iterable<Integer> iter) {
        return subscriber -> subscriber.onSubscribe(new Subscription() {
            @Override
            public void request(long n) {
                try {
                    iter.forEach(s -> subscriber.onNext(s));
                    subscriber.onComplete();
                } catch (Exception ex) {
                    subscriber.onError(ex);
                }
            }

            @Override
            public void cancel() {

            }
        });
    }

    private Subscriber<Integer> logSub() {
        return new Subscriber<>() {
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
                System.out.println("onError: " + throwable);
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        };
    }
}
