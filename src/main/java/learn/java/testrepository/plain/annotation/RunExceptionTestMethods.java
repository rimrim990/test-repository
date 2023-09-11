package learn.java.testrepository.plain.annotation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RunExceptionTestMethods {

    public static void main(String[] args) throws Exception {
        int tests = 0;
        int passed = 0;
        Class<?> testClass = Class.forName("learn.java.testrepository.plain.annotation.ExceptionTestMethodExample");
        for (Method m : testClass.getDeclaredMethods()) {
            if (m.isAnnotationPresent(ExceptionTestMethod.class)) {
                tests++;
                try {
                    m.invoke(null);
                    System.out.printf("테스트 %s 실패: 예외를 던지지 않음%n", m);
                } catch (InvocationTargetException wrappedEx) {
                    Throwable ex = wrappedEx.getCause();
                    Class<? extends Throwable> exType = m.getAnnotation(ExceptionTestMethod.class).value();
                    if (exType.isInstance(ex)) {
                        passed++;
                        continue;
                    }
                    System.out.printf("테스트 %s 실패: 기대한 예외 %s, 발생한 예외 %s%n", m, exType.getName(), ex);
                } catch (Exception ex) {
                    System.out.println("잘못 사용한 @ExceptionTestMethod: " + m);
                }
            }
        }
        System.out.printf("성공: %d, 실패: %d%n", passed, tests-passed);
    }
}
