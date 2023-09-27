package learn.java.testrepository.spring.aop;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@DisplayName("스프링 AOP 학습 테스트")
@SpringBootTest
class LoggerAdviceTest {

    @Autowired
    private LoggedService service;

    @Test
    @DisplayName("@Around 로 전반적인 메서드 실행 과정에 부가 기능을 적용할 수 있다")
    void aop_around_returning() {
        service.sayHi();
    }

    @Test
    @DisplayName("@Around 로 전반적인 메서드 실행 과정에 부가 기능을 적용할 수 있다")
    void aop_around_throwing() {
        // then
        assertThrows(RuntimeException.class, () -> service.throwRuntimeEx());
    }

    @Test
    @DisplayName("final 메서드는 오버라이딩이 불가능하므로 AOP 가 작용하지 않는다")
    void aop_finalMethod() {
        service.finalMethod();
    }

    @Test
    @DisplayName("protected 메서드에도 AOP 가 작용한다")
    void aop_protectedMethod() {
        service.protectedMethod();
    }

    @Test
    @DisplayName("default 메서드에도 AOP 가 작용한다")
    void aop_defaultMethod() {
        service.defaultMethod();
    }

    @Test
    @DisplayName("private 메서드에도 AOP 가 작용한다 ???")
    void aop_privateMethod() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final Class<? extends LoggedService> clazz = service.getClass();
        final Method privateMethod = clazz.getDeclaredMethod("privateMethod");
        privateMethod.setAccessible(true);
        privateMethod.invoke(service);
    }

    @Test
    @DisplayName("getDeclaredMethod 는 클래스에 선언된 메서드 정보를 가져온다")
    void getDeclareMethod() {
        final Child child = new Child();
        final Class<? extends Child> clazz = child.getClass();
        assertThrows(NoSuchMethodException.class, () -> clazz.getDeclaredMethod("privateMethod"));
    }

    @Test
    @DisplayName("AopContext 정보를 가져와 실행하면 자가호출 시에도 AOP 실행이 가능하다")
    void aop_selfInvocation() {
        service.selfInvocation();
    }

    private class Child extends LoggedService {

    }
}