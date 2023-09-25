package learn.java.testrepository.spring.cache;

import java.lang.reflect.Method;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

@Component
public class CustomKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        if (params.length > 0) {
            return params[0] + " " + method.getName();
        }
        return method.getName();
    }
}
