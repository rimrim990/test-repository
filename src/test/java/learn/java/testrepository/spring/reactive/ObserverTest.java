package learn.java.testrepository.spring.reactive;

import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("자바 옵저버 패턴 학습 테스트")
class ObserverTest {

    @Test
    @DisplayName("옵저버 패턴은 Push 방식으로 데이터를 밀어넣을 수 있다")
    void observer_pushData() {
        // given
        final Observer observer = (o, arg) -> System.out.println(arg);

        final IntObservable io = new IntObservable();
        io.addObserver(observer);

        // when
        io.run();
    }

    @Test
    @DisplayName("옵저버 패턴을 사용하여 Pub 스레드에서 등록된 Sub 메서드를 수행할 수 있다")
    void observer_thread() {
        // given
        final Observer observer = (o, arg) -> System.out.println(Thread.currentThread() + " : " + arg);

        final IntObservable io = new IntObservable();
        io.addObserver(observer);

        // when
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(io);

        System.out.println(Thread.currentThread() + " EXIT");
        executorService.shutdown();
    }

    @Test
    @DisplayName("옵저버 패턴 사용시 Sub 콜백 메서드 수행 시 예외가 발생해도 Sub 이 실행되는 스레드에 알려줄 수 없다")
    void observer_callbackException() {
        // given
        final Observer observer = (o, arg) -> {
            System.out.println(Thread.currentThread() + " : " + arg);
            throw new RuntimeException("exception thrown!");
        };

        final IntObservable io = new IntObservable();
        io.addObserver(observer);

        // when
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(io);

        System.out.println(Thread.currentThread() + " EXIT");
        executorService.shutdown();
    }


    @SuppressWarnings("deprecated")
    private static class IntObservable extends java.util.Observable implements Runnable {

        @Override
        public void run() {
            for (int i = 1; i <= 10; i++) {
                setChanged();
                // 이벤트를 등록된 Sub 에게 알린다
                notifyObservers(i);
            }
        }
    }
}