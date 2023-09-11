package learn.java.testrepository.plain.annotation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class RunTestMethods {

    public static void main(String[] args) throws Exception {
        int tests = 0;
        int passed = 0;
        Class<?> testClass = Class.forName("learn.java.testrepository.plain.annotation.TestMethodExample");
        for (Method m : testClass.getDeclaredMethods()) {
            if (m.isAnnotationPresent(TestMethod.class)) {
                tests++;
                try {
                    m.invoke(null);
                    passed++;
                } catch (InvocationTargetException wrappedEx) {
                    Throwable ex = wrappedEx.getCause();
                    System.out.println(m + " 실패: " + ex);
                } catch (Exception ex) {
                    System.out.println("잘못 사용한 @TestMethod: " + m);
                }
            }
        }
        System.out.printf("성공: %d, 실패: %d%n", passed, tests-passed);
    }
}
