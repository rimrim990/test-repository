package learn.java.testrepository.plain.synchronize;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("옵저버 패턴 테스트")
class ObservableSetTest {

    @Test
    @DisplayName("특정 메서드 호출이 발생했을 때 콜백 메서드를 호출받는다")
    void callback_event() {
        // given
        final Set<String> target = new HashSet<>();
        target.addAll(Arrays.asList("hello", "hi"));

        final ObservableSet<String> observer = new ObservableSet<>(target);

        // when
        observer.addObserver((s, e) -> System.out.println(e));

        // then
        for (int i = 0; i < 10; i++) {
            observer.add("hi " + i);
        }
    }

    @Test
    @DisplayName("특정 메서드 호출이 발생했을 때 구독을 해지한다 - 리스트 순회 중 리스트 원소를 삭제 시도하므로 예외 발생")
    void callback_cancelSub() {
        // given
        final Set<String> target = new HashSet<>();
        target.addAll(Arrays.asList("hello", "hi"));

        final ObservableSet<String> observer = new ObservableSet<>(target);

        // when
        // 람다는 자기 자신을 참조하지 못하므로 익명 클래스 사용
        observer.addObserver(new SetObserver<String>() {
            @Override
            public void added(ObservableSet<String> set, String element) {
                System.out.println(element);
                set.removeObserver(this);
            }
        });

        // then
        assertThrows(ConcurrentModificationException.class, () -> {
            for (int i = 0; i < 10; i++) {
                observer.add("hi " + i);
            }
        });
    }

    @Test
    @DisplayName("특정 메서드 호출이 발생했을 때 스레드를 생성하여 구독을 해지한다 - 데드락")
    void callback_cancelSub_withBackgroundThread() {
        // given
        final Set<String> target = new HashSet<>();
        target.addAll(Arrays.asList("hello", "hi"));

        final ObservableSet<String> observer = new ObservableSet<>(target);

        // when
        // 람다는 자기 자신을 참조하지 못하므로 익명 클래스 사용
        observer.addObserver(new SetObserver<String>() {
            @Override
            public void added(ObservableSet<String> set, String element) {
                System.out.println(element);
                ExecutorService exec = Executors.newSingleThreadExecutor();

                try {
                    exec.submit(() -> set.removeObserver(this)).get();
                } catch (ExecutionException | InterruptedException e) {
                    throw new AssertionError(e);
                } finally {
                    exec.shutdown();
                }
            }
        });

        // then
        for (int i = 0; i < 2; i++) {
            observer.add("hi " + i);
        }
    }
}