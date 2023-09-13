package learn.java.testrepository.plain.synchronize;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("자바 동기화 학습 테스트")
class SynchronizedTest {

    @Test
    @DisplayName("동기화하지 않은 데이터는 다른 스레드에서 읽을 수 있음을 보장받지 못한다")
    void withoutSynchronization_notSeen() throws InterruptedException {
        // given
        final StopThread thread = new StopThread();

        // 영원히 멈추지 않는다.
        thread.run();
    }

    @Test
    @DisplayName("동기화는 배타적 수행 뿐만 아니라 스레드 간 통신을 위해서도 필요하다")
    void withSynchronization_seen() {
        // given
        final SynchronizedStopThread thread = new SynchronizedStopThread();

        // when & then
        assertThatNoException()
            .isThrownBy(thread::run);
    }

    @Test
    @DisplayName("volatile 한정자는 가장 최근에 기록된 데이터를 가져오도록 보장한다")
    void withVolatile_latestData() {
        // given
        final VolatileStopThread thread = new VolatileStopThread();

        // when & then
        assertThatNoException()
            .isThrownBy(thread::run);
    }

    @Test
    @DisplayName("동기화가 없는 메서드 접근 테스트")
    void instance_withOutSynchronized() throws InterruptedException {
        // given
        final int count = 100;
        final Condition condition = new Condition();

        // when
        for (int i = 0; i < count; i++) {
            Thread thread = new Thread(() -> {
                System.out.println("Thread " + Thread.currentThread() +" trying to update: " + condition.getNumber());
                condition.increase();
                System.out.println("Thread " + Thread.currentThread() +" updated: " + condition.getNumber());
            });
            thread.start();
        }

        // then
        Thread.sleep(2000);
        assertThat(condition.getNumber()).isEqualTo(count);
    }

    @Test
    @DisplayName("인스턴스 synchronized 테스트")
    void instance_synchronized() throws InterruptedException {
        // given
        final int count = 100;
        final Condition condition = new Condition();

        // when
        for (int i = 0; i < count; i++) {
            Thread thread = new Thread(() -> {
                synchronized (condition) {
                    System.out.println("Thread " + Thread.currentThread() +" trying to update: " + condition.getNumber());
                    condition.increase();
                    System.out.println("Thread " + Thread.currentThread() +" updated: " + condition.getNumber());
                }
            });
            thread.start();
        }

        Thread.sleep(2000);
        assertThat(condition.getNumber()).isEqualTo(count);
    }

    @Test
    @DisplayName("동일한 인스턴스에 선언된 synchronized 메서드들은 인스턴스 락을 얻어야 하므로 동시에 실행될 수 없다")
    void method_synchronized() throws InterruptedException {
        final Condition condition = new Condition();

        // when
        Thread thread1 = new Thread(() -> {
            System.out.println("Thread " + Thread.currentThread() + "started");
            condition.lockMethod1();
        });
        thread1.start();

        Thread thread2 = new Thread(() -> {
            System.out.println("Thread " + Thread.currentThread() + "started");
            condition.lockMethod2();
        });
        thread2.start();

        Thread.sleep(2000);
    }

    @Test
    @DisplayName("서로 다른 인스턴스에 선언된 synchronized 메서드들은 동시에 실행될 수 있다")
    void method_synchronized_different_instance() throws InterruptedException {
        final Condition condition1 = new Condition();
        final Condition condition2 = new Condition();

        // when
        Thread thread1 = new Thread(() -> {
            System.out.println("Thread " + Thread.currentThread() + "started");
            condition1.lockMethod1();
        });
        thread1.start();

        Thread thread2 = new Thread(() -> {
            System.out.println("Thread " + Thread.currentThread() + "started");
            condition2.lockMethod2();
        });
        thread2.start();

        Thread.sleep(2000);
    }

    @Test
    @DisplayName("static 메서드에 선언된 synchronized 는 서로 다른 인스턴스 간에 동시 실행이 불가능하다")
    void static_synchronized_different_instance() throws InterruptedException {
        // when
        Thread thread1 = new Thread(() -> {
            System.out.println("Thread " + Thread.currentThread() + "started");
            Condition.staticLockMethod();
        });
        thread1.start();

        Thread thread2 = new Thread(() -> {
            System.out.println("Thread " + Thread.currentThread() + "started");
            Condition.staticLockMethod();
        });
        thread2.start();

        Thread.sleep(2000);
    }

    @Test
    @DisplayName("클래스에 대한 synchronized 는 서로 다른 인스턴스 간에 동시 실행이 불가능하다")
    void class_synchronized_different_instance() throws InterruptedException {
        // when
        Thread thread1 = new Thread(() -> {
            System.out.println("Thread " + Thread.currentThread() + "started");
            synchronized (Condition.class) {
                System.out.println("Thread " + Thread.currentThread() + " class synchronized");
            }
            System.out.println("Thread " + Thread.currentThread() + "finished");
        });
        thread1.start();

        Thread thread2 = new Thread(() -> {
            System.out.println("Thread " + Thread.currentThread() + "started");
            synchronized (Condition.class) {
                System.out.println("Thread " + Thread.currentThread() + " class synchronized");
            }
            System.out.println("Thread " + Thread.currentThread() + "finished");
        });
        thread2.start();

        Thread.sleep(2000);
    }

    static class Condition {
        private int number = 0;

        public int getNumber() {
            return number;
        }

        public static synchronized void staticLockMethod() {
            System.out.println("Thread " + Thread.currentThread() + ": staticLockMethod");
            System.out.println("Thread " + Thread.currentThread() + "finished");
        }

        public synchronized void lockMethod1() {
            System.out.println("Thread " + Thread.currentThread() + ": lockMethod1");
            System.out.println("Thread " + Thread.currentThread() + "finished");
        }

        public synchronized void lockMethod2() {
            System.out.println("Thread " + Thread.currentThread() + ": lockMethod2");
            System.out.println("Thread " + Thread.currentThread() + "finished");
        }

        public void increase() {
            number++;
        }
    }
}
