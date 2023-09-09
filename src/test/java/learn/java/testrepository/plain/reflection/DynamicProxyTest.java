package learn.java.testrepository.plain.reflection;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("JDK 다이내믹 프록시 테스트")
public class DynamicProxyTest {

    @Test
    @DisplayName("리플렉션으로 클래스 정보를 가져올 수 있다.")
    void reflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // given
        final String name = "Spring";

        // when
        final Method lengthMethod = name.getClass().getMethod("length");

        // then
        assertThat(lengthMethod.invoke(name)).isEqualTo(name.length());
    }

    @Test
    @DisplayName("JDB 다이나믹 프록시를 사용하여 인터페이스를 공유하는 프록시 객체를 생성할 수 있다")
    void dynamicProxy_shareInterface() {
        // given
        final Hello helloTarget = new HelloTarget();
        final InvocationHandler upperCaseHandler = new UpperCaseHandler(helloTarget);

        // when
        final Hello helloProxy = (Hello) Proxy.newProxyInstance(
            getClass().getClassLoader(),
            new Class[] { Hello.class },
            upperCaseHandler
        );

        // then
        assertThat(helloProxy.sayHello("test"))
            .isEqualTo(helloTarget.sayHello("test").toUpperCase());
    }
}
