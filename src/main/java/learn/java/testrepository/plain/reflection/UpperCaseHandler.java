package learn.java.testrepository.plain.reflection;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UpperCaseHandler implements InvocationHandler {

    // 메서드 호출을 위임하기 위해 타깃 오브젝트를 필드로 선언
    private final Object target;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 타깃으로 위임
        Object result = method.invoke(target, args);
        // 부가 기능 제공
        if (result instanceof String) {
            return ((String) result).toUpperCase();
        }
        return result;
    }
}
