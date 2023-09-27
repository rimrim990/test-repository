package learn.java.testrepository.spring.aop;

import java.lang.reflect.Method;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.core.SpringNamingPolicy;
import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.CallbackFilter;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

@DisplayName("CGLIB 프록시 학습 테스트")
public class CGLIBProxyTest {

    @Test
    @DisplayName("LoggedService 를 상속한 CGLIB 프록시 객체를 생성한다")
    void cglib() {
        // given
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(LoggedService.class);
        enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE); // $$SpringCGLIB$$
        enhancer.setCallback(new LoggingCallback());

        // when
        LoggedService proxy = (LoggedService) enhancer.create();

        // then
        proxy.defaultMethod(); // LoggedService$$SpringCGLIB$$0
    }

    @Test
    @DisplayName("CGLIB 프록시 인터셉터 실행 시, 어떤 콜백을 수행할지 필터링이 가능하다")
    void cglib_callbackFilter() {
        // given
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(LoggedService.class);
        enhancer.setNamingPolicy(SpringNamingPolicy.INSTANCE); // $$SpringCGLIB$$
        enhancer.setCallbacks(new Callback[]{new VoidCallback(), new LoggingCallback()});
        enhancer.setCallbackFilter(new LoggingCallbackFilter());

        // when
        LoggedService proxy = (LoggedService) enhancer.create();

        // then
        proxy.sayHi(); // LoggedService$$SpringCGLIB$$0
    }

    private class LoggingCallback implements MethodInterceptor {

        @Override
        public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
            System.out.println(method.getName() + " @Before");
            Object result = proxy.invokeSuper(obj, args);
            System.out.println(method.getName() + " @After");
            return result;
        }
    }

    private class VoidCallback implements MethodInterceptor {

        @Override
        public Object intercept(Object obj, Method method, Object[] args, MethodProxy proxy) throws Throwable {
            return proxy.invokeSuper(obj, args);
        }
    }

    private class LoggingCallbackFilter implements CallbackFilter {

        @Override
        public int accept(Method method) {
            if (method.getName().startsWith("say")) {
                return 1;
            }
            return 0;
        }
    }
}
