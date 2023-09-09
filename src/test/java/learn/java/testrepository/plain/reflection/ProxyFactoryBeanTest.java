package learn.java.testrepository.plain.reflection;

import static org.assertj.core.api.Assertions.assertThat;

import org.aopalliance.intercept.MethodInterceptor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactoryBean;

@DisplayName("스프링이 제공하는 프록시 팩토리 빈 테스트")
public class ProxyFactoryBeanTest {

    @Test
    @DisplayName("프록시 빈 생성 테스트")
    void create_proxyFactoryBean() {
        // given
        final Hello helloTarget = new HelloTarget();
        final MethodInterceptor upperCaseAdvice = new UppercaseAdvice();

        // when
        final ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(helloTarget);
        pfBean.addAdvice(upperCaseAdvice);

        final Hello helloProxy = (Hello) pfBean.getObject();

        // then
        assertThat(helloProxy.sayHi("test"))
            .isEqualTo(helloTarget.sayHi("test").toUpperCase());
    }
}
